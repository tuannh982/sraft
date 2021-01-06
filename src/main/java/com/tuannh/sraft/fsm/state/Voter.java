package com.tuannh.sraft.fsm.state;

import com.tuannh.sraft.dto.rpc.RequestVote;
import com.tuannh.sraft.dto.rpc.RequestVoteResponse;
import com.tuannh.sraft.fsm.RaftFsm;
import com.tuannh.sraft.server.RaftData;
import com.tuannh.sraft.server.RaftServer;
import lombok.extern.log4j.Log4j2;

@Log4j2
public abstract class Voter extends ServerState {
    protected String votedFor = null;

    protected Voter(RaftServer server, RaftFsm fsm, RaftData data) {
        super(server, fsm, data);
    }

    @Override
    public synchronized boolean handle(RequestVote message) {
        log.info("{} | server received vote request {}", server.getId() + state, message);
        String serverId = server.getId();
        if (votedFor == null && message.getLastLogIndex() >= data.getLastLogIndex().get()) {
            votedFor = message.getCandidateId();
            server.getNetwork().sendMsg(serverId, message.getFrom(), new RequestVoteResponse(
                    serverId,
                    data.getCurrentTerm().get(),
                    true
            ));
        } else {
            server.getNetwork().sendMsg(serverId, message.getFrom(), new RequestVoteResponse(
                    serverId,
                    data.getCurrentTerm().get(),
                    false
            ));
        }
        return true;
    }
}
