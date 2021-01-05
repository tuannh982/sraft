package com.tuannh.sraft.dto.rpc;

import lombok.Getter;

@Getter
public class Response extends BaseRpc {
    private final long term;
    private final boolean status; // success for AppendEntriesRPC, voteGranted for RequestVoteRPC

    public Response(String from, long term, boolean status) {
        super(from);
        this.term = term;
        this.status = status;
    }
}
