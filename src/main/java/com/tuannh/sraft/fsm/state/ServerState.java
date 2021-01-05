package com.tuannh.sraft.fsm.state;

import com.tuannh.sraft.dto.rpc.RpcVisitor;
import com.tuannh.sraft.fsm.RaftFsm;
import lombok.Getter;

public abstract class ServerState implements RpcVisitor {
    private RaftFsm fsm;
    //
    @Getter
    protected FsmState state;
    // Persistent state on all servers
    protected long currentTerm;
    protected String votedFor;
    protected Object[] logs; // FIXME
    // Volatile state on all servers
    protected long commitIndex;
    protected long lastApplied;
}
