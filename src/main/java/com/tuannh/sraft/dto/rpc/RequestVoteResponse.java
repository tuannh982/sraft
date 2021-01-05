package com.tuannh.sraft.dto.rpc;

import lombok.Getter;

@Getter
public class RequestVoteResponse extends BaseRpc {
    private final long term;
    private final boolean voteGranted;

    public RequestVoteResponse(String from, long term, boolean voteGranted) {
        super(from);
        this.term = term;
        this.voteGranted = voteGranted;
    }
}
