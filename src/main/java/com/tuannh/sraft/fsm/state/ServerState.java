package com.tuannh.sraft.fsm.state;

import com.tuannh.sraft.dto.rpc.RpcVisitor;
import com.tuannh.sraft.fsm.RaftFsm;
import com.tuannh.sraft.server.RaftData;
import com.tuannh.sraft.server.RaftServer;
import lombok.Getter;

public abstract class ServerState implements RpcVisitor {
    protected final RaftServer server;
    protected final RaftFsm fsm;
    protected final RaftData data;
    @Getter
    protected FsmState state;

    protected ServerState(RaftServer server, RaftFsm fsm, RaftData data) {
        this.server = server;
        this.fsm = fsm;
        this.data = data;
    }
}
