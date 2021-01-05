package com.tuannh.sraft.network;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

public interface Network<I extends Serializable> {
    void register(I id);
    void deregister(I id);
    <T extends Serializable> void broadcast(I from, T message);
    <T extends Serializable> void sendMsg(I from, I to, T message);
    <T extends Serializable> T pollMsg(I id, long timeout, TimeUnit unit) throws InterruptedException;
}
