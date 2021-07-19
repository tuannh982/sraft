package com.github.tuannh982.sraft.utility.timer;

public interface Timer {
    long getTimeout();
    void reset(long timeout);
    void reset();
    void stop();
}
