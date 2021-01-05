package com.tuannh.sraft.server;

import com.tuannh.sraft.dto.rpc.*;
import com.tuannh.sraft.fsm.RaftFsm;
import com.tuannh.sraft.fsm.event.FsmEvent;
import com.tuannh.sraft.utility.network.Network;
import lombok.Getter;

@Getter
public class RaftServer implements RpcVisitor {
    private final String id;
    private final int quorumSize;
    private final int minimalVoteCount;
    private final Network<String> network;
    private final RaftFsm fsm;
    private final RaftData data;

    public RaftServer(String id, int quorumSize, Network<String> network) {
        this.id = id;
        this.quorumSize = quorumSize;
        minimalVoteCount = (quorumSize + 1) / 2;
        this.network = network;
        data = new RaftData();
        fsm = new RaftFsm(this);
    }

    // handle message

    @Override
    public void preHandle(BaseRpc o) {
        long currentTerm = data.getCurrentTerm().get();
        if (o.getTerm() > currentTerm) {
            fsm.transition(FsmEvent.HIGHER_TERM_DISCOVERED);
        } else if (o.getTerm() < currentTerm) {
            network.sendMsg(id, o.getFrom(), new AppendEntriesResponse(
                    id,
                    currentTerm,
                    false
            ));
        }
    }

    @Override
    public void handle(AppendEntries message) {
        fsm.getState().handle(message);
    }

    @Override
    public void handle(AppendEntriesResponse message) {
        fsm.getState().handle(message);
    }

    @Override
    public void handle(RequestVote message) {
        fsm.getState().handle(message);
    }

    @Override
    public void handle(RequestVoteResponse message) {
        fsm.getState().handle(message);
    }
}
