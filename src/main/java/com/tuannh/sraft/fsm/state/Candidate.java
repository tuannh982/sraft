package com.tuannh.sraft.fsm.state;

import com.tuannh.sraft.dto.rpc.AppendEntries;
import com.tuannh.sraft.dto.rpc.AppendEntriesResponse;
import com.tuannh.sraft.dto.rpc.RequestVote;
import com.tuannh.sraft.dto.rpc.RequestVoteResponse;
import com.tuannh.sraft.fsm.RaftFsm;
import com.tuannh.sraft.fsm.event.FsmEvent;
import com.tuannh.sraft.server.RaftData;
import com.tuannh.sraft.server.RaftServer;

import java.util.HashSet;
import java.util.Set;

public class Candidate extends Voter {
    private final Set<String> votes = new HashSet<>();

    public Candidate(RaftServer server, RaftFsm fsm, RaftData data) {
        super(server, fsm, data);
        startElection();
    }

    @Override
    public void handle(AppendEntries message) {
        // NOOP
    }

    @Override
    public void handle(AppendEntriesResponse message) {
        // NOOP
    }

    @Override
    public void handle(RequestVote message) {
        // candidate do nothing
    }

    @Override
    public void handle(RequestVoteResponse message) {
        if (message.isVoteGranted()) {
            votes.add(message.getFrom());
            if (votes.size() >= server.getMinimalVoteCount()) {
                fsm.transition(FsmEvent.MAJORITY_VOTE_RECEIVED);
            }
        }
    }

    private void startElection() {
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
