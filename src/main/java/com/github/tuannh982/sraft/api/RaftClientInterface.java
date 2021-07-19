package com.github.tuannh982.sraft.api;

public interface RaftClientInterface {
    void deposit(long value);
    void withdraw(long value);
}
