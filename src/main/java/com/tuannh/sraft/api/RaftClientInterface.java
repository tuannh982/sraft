package com.tuannh.sraft.api;

public interface RaftClientInterface {
    void deposit(long value);
    void withdraw(long value);
}
