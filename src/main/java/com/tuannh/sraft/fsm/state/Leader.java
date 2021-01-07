package com.tuannh.sraft.fsm.state;

import com.tuannh.sraft.dto.rpc.AppendEntries;
import com.tuannh.sraft.dto.rpc.AppendEntriesResponse;
import com.tuannh.sraft.dto.rpc.RequestVote;
import com.tuannh.sraft.dto.rpc.RequestVoteResponse;
import com.tuannh.sraft.fsm.RaftFsm;
import com.tuannh.sraft.log.RaftLog;
import com.tuannh.sraft.server.RaftData;
import com.tuannh.sraft.server.RaftServer;
import lombok.extern.log4j.Log4j2;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Log4j2
public class Leader extends ServerState {
    private final Map<String, AtomicLong> nextIndex = new HashMap<>();
    private final Map<String, AtomicLong> matchIndex = new HashMap<>();

    public Leader(RaftServer server, RaftFsm fsm, RaftData data) {
        super(server, fsm, data);
        state = FsmState.LEADER;
        server.getTimer().reset(server.getHeartbeatTimeout());
        sendHeartBeat(server.getId());
        for (String neighbor : server.getNeighbors()) {
            nextIndex.put(neighbor, new AtomicLong(data.getLastLogIndex().get() + 1));
            matchIndex.put(neighbor, new AtomicLong(-1));
        }
    }

    public synchronized void heartbeat() {
        sendHeartBeat(server.getId());
    }

    private synchronized void sendHeartBeat(String serverId) {
        log.info("{} | server send heartbeat", server.getId() + state);
        AppendEntries heartbeat = new AppendEntries(
                serverId,
                data.getCurrentTerm().get(),
                serverId,
                data.getLastLogIndex().get(),
                data.getLastLogTerm().get(),
                new RaftLog[0],
                data.getCommitIndex().get()
        );
        for (String neighbor : fsm.getServer().getQuorum()) {
            fsm.getServer().getNetwork().sendMsg(serverId, neighbor, heartbeat);
        }
    }

    @Override
    public boolean handle(AppendEntries message) {
        // NOOP
        return false;
    }

    @Override
    public synchronized boolean handle(AppendEntriesResponse message) {
        final String sender = message.getFrom();
        if (message.isSuccess()) {
            matchIndex.get(sender).set(nextIndex.get(sender).get());
            nextIndex.get(sender).addAndGet(message.getSize());
            if (nextIndex.get(sender).get() > data.getLastLogIndex().get() + 1) {
                nextIndex.get(sender).set(data.getLastLogIndex().get() + 1);
            }
        }
        // nelse
        {
            final String serverId = server.getId();
            if (data.getLastLogIndex().get() >= nextIndex.get(sender).get()) {
                log.info("{} | append entries to {} (last_log_index = {} >= next_index = {})",
                        server.getId() + state,
                        sender,
                        data.getLastLogIndex().get(),
                        nextIndex.get(sender).get());
                final long prevIndex = nextIndex.get(sender).get() - 1;
                RaftLog currentLog;
                long prevTerm = 0;
                if (prevIndex < 0) {
                    currentLog = data.getLogs().getLast();
                } else {
                    prevTerm = data.getLogs().get((int) prevIndex).getTerm();
                    currentLog = data.getLogs().get((int) (prevIndex + 1)); // FIXME
                }
                AppendEntries msg = new AppendEntries(
                        serverId,
                        data.getCurrentTerm().get(),
                        serverId,
                        prevIndex,
                        prevTerm,
                        new RaftLog[] {currentLog},
                        data.getCommitIndex().get()
                );
                log.info("{} | server send heartbeat to {} with data {}", server.getId() + state, sender, msg);
                server.getNetwork().sendMsg(serverId, sender, msg);
            }
            return true;
        }
    }

    @Override
    public boolean handle(RequestVote message) {
        // NOOP
        return false;
    }

    @Override
    public boolean handle(RequestVoteResponse message) {
        // NOOP
        return false;
    }
}
