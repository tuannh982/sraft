package com.github.tuannh982.sraft.utility.network;

public interface Network<I> {
    void register(I id);
    void deregister(I id);
    <T> void broadcast(I from, T message);
    <T> void sendMsg(I from, I to, T message);
    <T> T pollMsg(I id) throws InterruptedException;
    void jamConnection(JammingType jammingType, double value, I from, I to);
    void clearJam(I from, I to);
}
