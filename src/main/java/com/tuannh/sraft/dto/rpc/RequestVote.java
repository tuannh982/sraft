package com.tuannh.sraft.dto.rpc;

import lombok.Getter;

@Getter
public class RequestVote extends BaseRpc {
    private final String candidateId;
    private final long lastLogIndex;
    private final long lastLogTerm;

    public RequestVote(String from, long term, String candidateId, long lastLogIndex, long lastLogTerm) {
        super(from, term);
        this.candidateId = candidateId;
        this.lastLogIndex = lastLogIndex;
        this.lastLogTerm = lastLogTerm;
    }
}
