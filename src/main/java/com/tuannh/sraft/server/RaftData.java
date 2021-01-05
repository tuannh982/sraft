package com.tuannh.sraft.server;

import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.atomic.AtomicLong;

@Getter
@Setter
public final class RaftData {
    // Persistent state on all servers
    private AtomicLong currentTerm;
    private Object[] logs; // FIXME
    // Volatile state on all servers
    private AtomicLong commitIndex;
    private AtomicLong lastApplied;
    //
    private AtomicLong lastLogIndex;
    private AtomicLong lastLogTerm;

    public RaftData() {
        currentTerm = new AtomicLong(0);
        logs = new Object[0];
        commitIndex = new AtomicLong(0);
        lastApplied = new AtomicLong(0);
        lastLogIndex = new AtomicLong(0);
        lastLogTerm = new AtomicLong(0);
    }
}
