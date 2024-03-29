package com.github.tuannh982.sraft.dto.rpc;

import lombok.Getter;
import lombok.ToString;

@ToString(callSuper = true)
@Getter
public class RequestVoteResponse extends BaseRpc {
    private final boolean voteGranted;

    public RequestVoteResponse(String from, long term, boolean voteGranted) {
        super(from, term);
        this.voteGranted = voteGranted;
    }
}
