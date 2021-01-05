package com.tuannh.sraft.utility.timer;

public interface Timer {
    long getTimeout();
    void reset(long timeout);
    void reset();
    void stop();
}
