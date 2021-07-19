package com.github.tuannh982.sraft.dto.rpc;

import com.github.tuannh982.sraft.dto.BaseMessage;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public abstract class BaseRpc extends BaseMessage {
    private final String from;
    private final long term;

    protected BaseRpc(String from, long term) {
        this.from = from;
        this.term = term;
    }
}
