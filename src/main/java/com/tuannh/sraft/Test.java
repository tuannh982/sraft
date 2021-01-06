package com.tuannh.sraft;

import com.tuannh.sraft.server.RaftServer;
import com.tuannh.sraft.utility.network.Network;
import com.tuannh.sraft.utility.network.SimpleQueueBasedNetwork;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Test {
    public static void main(String[] args) throws InterruptedException {
        Network<String> internet = new SimpleQueueBasedNetwork<>();
        List<String> quorum = Arrays.asList("1", "2", "3");
        List<RaftServer> rs = new ArrayList<>();
        internet.register("1");
        internet.register("2");
        internet.register("3");
        Thread ts1 = new Thread(() -> rs.add(new RaftServer("1", quorum, internet)));
        Thread ts2 = new Thread(() -> rs.add(new RaftServer("2", quorum, internet)));
        Thread ts3 = new Thread(() -> rs.add(new RaftServer("3", quorum, internet)));
        ts1.start();
        ts2.start();
        ts3.start();
        Thread.sleep(500);
        Thread st = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                System.out.println("-----------------------------------------------------------------------------------");
                for (RaftServer s : rs) {
                    System.out.println("server " + s.getId() + "|" + s.getFsm().getState().getState() + "|" + s.getData());
                }
                System.out.println("-----------------------------------------------------------------------------------");
            }
        });
        st.start();
    }
}
