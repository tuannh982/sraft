package com.github.tuannh982.sraft.dto.rpc;

import com.github.tuannh982.sraft.log.RaftLog;
import lombok.Getter;
import lombok.ToString;

@ToString(callSuper = true)
@Getter
public class AppendEntries extends BaseRpc {
    private final String leaderId;
    private final long prevLogIndex;
    private final long prevLogTerm;
    private final RaftLog[] entries;
    private final long leaderCommit;

    public AppendEntries(String from, long term, String leaderId, long prevLogIndex, long prevLogTerm, RaftLog[] entries, long leaderCommit) {
        super(from, term);
        this.leaderId = leaderId;
        this.prevLogIndex = prevLogIndex;
        this.prevLogTerm = prevLogTerm;
        this.entries = entries;
        this.leaderCommit = leaderCommit;
    }
}
