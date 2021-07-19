package com.github.tuannh982.sraft;

import com.github.tuannh982.sraft.server.RaftServer;
import com.github.tuannh982.sraft.utility.network.Network;
import com.github.tuannh982.sraft.utility.network.SimpleQueueBasedNetwork;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Test {
    public static void main(String[] args) throws InterruptedException {
        Network<String> internet = new SimpleQueueBasedNetwork<>();
        List<String> quorum = Arrays.asList("1", "2", "3");
        List<RaftServer> rs = Collections.synchronizedList(new ArrayList<>());
        internet.register("1");
        internet.register("2");
        internet.register("3");
        Thread ts1 = new Thread(() -> {
            RaftServer s = new RaftServer("1", quorum, internet);
            rs.add(s);
        });
        Thread ts2 = new Thread(() -> {
            RaftServer s = new RaftServer("2", quorum, internet);
            rs.add(s);
        });
        Thread ts3 = new Thread(() -> {
            RaftServer s = new RaftServer("3", quorum, internet);
            rs.add(s);
        });
        ts1.start();
        ts2.start();
        ts3.start();
        Thread.sleep(500);
        Thread st = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                System.out.println("-----------------------------------------------------------------------------------");
                System.out.println("SIZE = " + rs.size());
                for (RaftServer s : rs) {
                    System.out.println("server " + s.getId() + "|" + s.getFsm().getState().getState() + "|" + s.getData());
                }
                System.out.println("-----------------------------------------------------------------------------------");
            }
        });
        st.start();
        AtomicInteger nt = new AtomicInteger(5);
        Thread st2 = new Thread(() -> {
            while (nt.get() > 0) {
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                for (RaftServer s : rs) {
                    s.deposit(100L * nt.get());
                }
                nt.decrementAndGet();
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.exit(0);
        });
        st2.start();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("-SERVER-SUMMARY--------------------------------------------------------------------");
            System.out.println("SIZE = " + rs.size());
            for (RaftServer s : rs) {
                System.out.println("server " + s.getId() + "|" + s.getFsm().getState().getState() + "|" + s.getData());
            }
            System.out.println("-----------------------------------------------------------------------------------");
        }));
    }
}
