package com.tuannh.sraft.commons.rand;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Random {
    public static long rand(long initial, long overhead) {
        return (long) (initial + (Math.random() - 0.5) * overhead);
    }
}
