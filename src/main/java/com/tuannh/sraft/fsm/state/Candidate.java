package com.tuannh.sraft.fsm.state;

import com.tuannh.sraft.dto.rpc.*;
import com.tuannh.sraft.fsm.RaftFsm;
import com.tuannh.sraft.fsm.event.FsmEvent;
import com.tuannh.sraft.server.RaftData;
import com.tuannh.sraft.server.RaftServer;
import lombok.extern.log4j.Log4j2;

import java.util.HashSet;
import java.util.Set;

@Log4j2
public class Candidate extends Voter {
    private final Set<String> votes = new HashSet<>();

    public Candidate(RaftServer server, RaftFsm fsm, RaftData data) {
        super(server, fsm, data);
        state = FsmState.CANDIDATE;
        votes.add(server.getId()); // self-voted
        startElection();
    }

    @Override
    public boolean handle(AppendEntries message) {
        // NOOP
        return false;
    }

    @Override
    public boolean handle(AppendEntriesResponse message) {
        // NOOP
        return false;
    }

    @Override
    public boolean handle(RequestVote message) {
        // candidate do nothing
        return true;
    }

    @Override
    public synchronized boolean handle(RequestVoteResponse message) {
        log.info("{} | received RequestVoteResponse, granted = {}", server.getId() + state, message.isVoteGranted());
        if (message.isVoteGranted()) {
            votes.add(message.getFrom());
            log.info("{} | current vote count = {}", server.getId() + state, votes.size());
            if (votes.size() >= server.getMinimalVoteCount()) {
                fsm.transition(FsmEvent.MAJORITY_VOTE_RECEIVED);
            }
        }
        return true;
    }

    private void startElection() {
        log.info("{} | start election", server.getId() + state);
        data.getCurrentTerm().incrementAndGet();
        String serverId = server.getId();
        RequestVote requestVote = new RequestVote(
                serverId,
                data.getCurrentTerm().get(),
                serverId,
                data.getLastLogIndex().get(),
                data.getLastLogTerm().get()
        );
        fsm.getServer().getNetwork().broadcast(serverId, requestVote);
        votedFor = serverId;
    }
}
