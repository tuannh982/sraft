package com.tuannh.sraft.server;

import com.tuannh.sraft.log.RaftLog;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicLong;

@ToString
@Getter
@Setter
public final class RaftData {
    // Persistent state on all servers
    private AtomicLong currentTerm;
    private LinkedList<RaftLog> logs;
    // Volatile state on all servers
    private AtomicLong commitIndex;
    private AtomicLong lastApplied;
    //
    private AtomicLong lastLogIndex;
    private AtomicLong lastLogTerm;

    public RaftData() {
        currentTerm = new AtomicLong(0);
        logs = new LinkedList<>();
        commitIndex = new AtomicLong(-1);
        lastApplied = new AtomicLong(-1);
        lastLogIndex = new AtomicLong(-1);
        lastLogTerm = new AtomicLong(0);
    }
}
