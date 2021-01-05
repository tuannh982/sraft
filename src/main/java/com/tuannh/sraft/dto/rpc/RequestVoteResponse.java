package com.tuannh.sraft.dto.rpc;

import lombok.Getter;

@Getter
public class RequestVoteResponse extends BaseRpc {
    private final boolean voteGranted;

    public RequestVoteResponse(String from, long term, boolean voteGranted) {
        super(from, term);
        this.voteGranted = voteGranted;
    }
}
