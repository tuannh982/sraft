package com.tuannh.sraft.network;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@Getter
@AllArgsConstructor
public final class Packet<I extends Serializable, T> {
    private final I sender;
    private final I receiver;
    private final T payload;
    private final long ts = System.currentTimeMillis();
}
