package com.tuannh.sraft.dto.rpc;

import com.tuannh.sraft.dto.BaseMessage;
import lombok.Getter;

@Getter
public abstract class BaseRpc extends BaseMessage {
    private final String from;
    private final long term;

    protected BaseRpc(String from, long term) {
        this.from = from;
        this.term = term;
    }
}
