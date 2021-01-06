package com.tuannh.sraft.dto.rpc;

import lombok.Getter;
import lombok.ToString;

@ToString(callSuper = true)
@Getter
public class AppendEntriesResponse extends BaseRpc {
    private final boolean success;

    public AppendEntriesResponse(String from, long term, boolean success) {
        super(from, term);
        this.success = success;
    }
}
