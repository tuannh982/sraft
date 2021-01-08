package com.tuannh.sraft.utility.network;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
@AllArgsConstructor
public final class Packet<I, T> {
    private final I sender;
    private final I receiver;
    private final T payload;
    private final long ts = System.currentTimeMillis();
}
