package com.tuannh.sraft.fsm.state;

import com.tuannh.sraft.commons.rand.Random;
import com.tuannh.sraft.dto.rpc.*;
import com.tuannh.sraft.fsm.RaftFsm;
import com.tuannh.sraft.log.RaftLog;
import com.tuannh.sraft.server.RaftData;
import com.tuannh.sraft.server.RaftServer;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class Follower extends Voter {
    public Follower(RaftServer server, RaftFsm fsm, RaftData data) {
        super(server, fsm, data);
        state = FsmState.FOLLOWER;
        server.getTimer().reset(Random.rand(server.getElectionTimeout(), server.getOverhead()));
    }

    private synchronized void replyAppendEntries(String serverId, String dest, long len, boolean success) {
        server.getNetwork().sendMsg(serverId, dest, new AppendEntriesResponse(
                serverId,
                data.getCurrentTerm().get(),
                len,
                success
        ));
    }

    @Override
    public synchronized boolean handle(AppendEntries message) {
        server.getTimer().reset();
        final boolean isHeartbeat = message.getEntries().length == 0;
        log.info("{} | received AppendEntries request (heartbeat={}), reset timer", server.getId() + state, isHeartbeat);
        final String serverId = server.getId();
        final String dest = message.getFrom();
        {
            // ---------------------------------------------------------------------------------------------------------
            if (message.getLeaderCommit() != data.getCommitIndex().get()) {
                log.info("{} | message leader commit != commit index ({} != {})", server.getId() + state, message.getLeaderCommit(), data.getCommitIndex().get());
                data.getCommitIndex().set(Math.min(message.getLeaderCommit(), data.getLogs().size() - 1)); // FIXME
            }
            // ---------------------------------------------------------------------------------------------------------
            if (data.getLogs().size() < message.getPrevLogIndex()) {
                log.info("{} | server log ({}) not match message prev_log_index({})", server.getId() + state, data.getLogs().size() - 1, message.getPrevLogIndex());
                replyAppendEntries(serverId, dest, 0, false);
                return true;
            }
            // ---------------------------------------------------------------------------------------------------------
            if (
                    !data.getLogs().isEmpty() &&
                    (
                            (message.getPrevLogIndex() >= data.getLogs().size()) ||
                            (data.getLogs().get((int) message.getPrevLogIndex()).getTerm() != message.getPrevLogTerm())
                    )
            ) { // FIXME
                // truncate logs
                if ((message.getPrevLogIndex() < data.getLogs().size())) {
                    log.info("{} | server prev_log_term not match message prev_log_term, truncate server logs from message prev_log_term", server.getId() + state);
                    replyAppendEntries(serverId, dest, 0, false);
                    data.getLogs().subList((int) message.getPrevLogIndex(), data.getLogs().size()).clear(); // FIXME
                    data.getLastLogIndex().set(message.getPrevLogIndex());
                    data.getLastLogTerm().set(message.getPrevLogTerm());
                    return true;
                }
            }
            // ---------------------------------------------------------------------------------------------------------
            final long msgLeaderCommit = message.getLeaderCommit();
            if (
                    !data.getLogs().isEmpty() && msgLeaderCommit > 0 &&
                    (
                            msgLeaderCommit >= data.getLogs().size() ||
                            data.getLogs().get((int) msgLeaderCommit).getTerm() != message.getTerm()
                    )
            ) { // FIXME
                if (msgLeaderCommit < data.getLogs().size()) {
                    log.info("{} | server log_term not match message log_term, truncate server logs from latest commit_index", server.getId() + state);
                    data.getLogs().subList((int) data.getCommitIndex().get(), data.getLogs().size()).clear(); // FIXME
                    replyAppendEntries(serverId, dest, message.getEntries().length, true);
                    log.info("{} | append logs to server {}", server.getId() + state, message.getEntries());
                    for (RaftLog log : message.getEntries()) {
                        data.getLogs().add(log);
                    }
                    log.info("{} | server logs after append {}", server.getId() + state, data.getLogs() );
                    data.getLastLogIndex().set(data.getLogs().size() - 1);
                    data.getLastLogTerm().set(data.getLogs().getLast().getTerm());
                    data.getCommitIndex().set(data.getLogs().size() - 1);
                    data.getLastApplied().set(data.getLastLogIndex().get());
                    return true;
                }
            }
            // normal
            if (isHeartbeat) {
                log.info("{} | heartbeat received", server.getId() + state);
                replyAppendEntries(serverId, dest, 0, true);
                return true;
            } else {
                log.info("{} | append logs to server {}", server.getId() + state, message.getEntries());
                for (RaftLog log : message.getEntries()) {
                    data.getLogs().add(log);
                }
                log.info("{} | server logs after append {}", server.getId() + state, data.getLogs());
                data.getLastLogIndex().set(data.getLogs().size() - 1);
                data.getLastLogTerm().set(data.getLogs().getLast().getTerm());
                data.getCommitIndex().set(data.getLogs().size() - 1);
                data.getLastApplied().set(data.getLastLogIndex().get());
                replyAppendEntries(serverId, dest, message.getEntries().length, true);
            }
            return true;
        }
    }

    @Override
    public boolean handle(AppendEntriesResponse message) {
        // NOOP
        return false;
    }

    @Override
    public boolean handle(RequestVoteResponse message) {
        // NOOP
        return false;
    }
}
