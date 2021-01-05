package com.tuannh.sraft.network;

import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

public class SimpleQueueBasedNetworkTest {
    @Test
    public void simpleTest0() throws InterruptedException {
        Network<String> network = new SimpleQueueBasedNetwork<>();
        network.register("A");
        network.register("B");
        network.sendMsg("A", "B", "msg1");
        String msg = network.pollMsg("B", 100, TimeUnit.MILLISECONDS);
        assertEquals("msg1", msg);
        network.sendMsg("A", "C", "msg2");
        String msgN = network.pollMsg("C", 100, TimeUnit.MILLISECONDS);
        assertNull(msgN);
    }
}
