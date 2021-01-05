package com.tuannh.sraft.dto.rpc;

import lombok.Getter;

@Getter
public class AppendEntries extends BaseRpc {
    private final long term;
    private final String leaderId;
    private final long prevLogIndex;
    private final long prevLogTerm;
    private final Object[] entries; // FIXME
    private final long leaderCommit;

    public AppendEntries(String from, long term, String leaderId, long prevLogIndex, long prevLogTerm, Object[] entries, long leaderCommit) {
        super(from);
        this.term = term;
        this.leaderId = leaderId;
        this.prevLogIndex = prevLogIndex;
        this.prevLogTerm = prevLogTerm;
        this.entries = entries;
        this.leaderCommit = leaderCommit;
    }
}
