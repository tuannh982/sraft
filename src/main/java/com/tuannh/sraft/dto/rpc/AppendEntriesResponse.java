package com.tuannh.sraft.dto.rpc;

import lombok.Getter;

@Getter
public class AppendEntriesResponse extends BaseRpc {
    private final long term;
    private final boolean success;

    public AppendEntriesResponse(String from, long term, boolean success) {
        super(from);
        this.term = term;
        this.success = success;
    }
}
