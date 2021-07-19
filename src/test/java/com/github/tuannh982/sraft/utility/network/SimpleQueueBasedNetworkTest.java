package com.github.tuannh982.sraft.utility.network;

import org.junit.Test;

import static org.junit.Assert.*;

public class SimpleQueueBasedNetworkTest {
    @Test
    public void simpleTest0() throws InterruptedException {
        Network<String> network = new SimpleQueueBasedNetwork<>();
        network.register("A");
        network.register("B");
        network.sendMsg("A", "B", "msg1");
        String msg = network.pollMsg("B");
        assertEquals("msg1", msg);
        network.sendMsg("A", "C", "msg2");
        String msgN = network.pollMsg("C");
        assertNull(msgN);
    }

    @Test
    public void latencyJammingTest0() throws InterruptedException {
        Network<String> network = new SimpleQueueBasedNetwork<>();
        network.register("A");
        network.register("B");
        // insert latency jamming
        network.clearJam("A", "B");
        network.jamConnection(JammingType.LATENCY, 2000, "A", "B");
        long start0 = System.currentTimeMillis();
        network.sendMsg("A", "B", "msg1");
        String msg1 = network.pollMsg("B");
        long stop0 = System.currentTimeMillis();
        assertTrue(stop0 - start0 > 1900);
    }

    @Test
    public void lossJammingTest0() throws InterruptedException {
        Network<String> network = new SimpleQueueBasedNetwork<>();
        network.register("A");
        network.register("B");
        // packet drop jamming
        network.clearJam("A", "B");
        double lossRate = 0.8;
        network.jamConnection(JammingType.LOSS, lossRate, "A", "B");
        int nPackets = 10_000;
        double error = 0.05; // 5% error
        int nErrorCount = (int) (error * nPackets);
        for (int i = 0; i < nPackets; i++) {
            network.sendMsg("A", "B", "msg1");
        }
        int lossCount = 0;
        for (int i = 0; i < nPackets; i++) {
            String tempMsg = network.pollMsg("B");
            if (tempMsg == null) lossCount++;
        }
        int expectedLoss = (int) (lossRate * nPackets);
        assertTrue((lossCount >= (expectedLoss - nErrorCount)) && (lossCount <= (expectedLoss + nErrorCount)));
    }
}
