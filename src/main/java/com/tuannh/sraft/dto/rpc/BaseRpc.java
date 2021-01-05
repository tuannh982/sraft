package com.tuannh.sraft.dto.rpc;

import com.tuannh.sraft.dto.BaseMessage;
import lombok.Getter;

@Getter
public abstract class BaseRpc extends BaseMessage {
    private final String from;

    protected BaseRpc(String from) {
        this.from = from;
    }
}
