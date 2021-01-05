package com.tuannh.sraft.state;

import com.tuannh.sraft.dto.rpc.RpcVisitor;

public abstract class ServerState implements RpcVisitor {
    // Persistent state on all servers
    private long currentTerm;
    private String votedFor;
    private Object[] logs; // FIXME
    // Volatile state on all servers
    private long commitIndex;
    private long lastApplied;
}
