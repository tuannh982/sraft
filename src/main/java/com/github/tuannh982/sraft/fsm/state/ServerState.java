package com.github.tuannh982.sraft.fsm.state;

import com.github.tuannh982.sraft.fsm.RaftFsm;
import com.github.tuannh982.sraft.server.RaftData;
import com.github.tuannh982.sraft.server.RaftServer;
import com.github.tuannh982.sraft.dto.rpc.RpcVisitor;
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
