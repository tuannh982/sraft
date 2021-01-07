package com.tuannh.sraft.dto.rpc;

import lombok.Getter;
import lombok.ToString;

@ToString(callSuper = true)
@Getter
public class AppendEntriesResponse extends BaseRpc {
    private final long size;
    private final boolean success;

    public AppendEntriesResponse(String from, long term, long size, boolean success) {
        super(from, term);
        this.size = size;
        this.success = success;
    }
}
