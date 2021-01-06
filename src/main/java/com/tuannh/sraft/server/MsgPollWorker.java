package com.tuannh.sraft.server;

import com.tuannh.sraft.dto.rpc.BaseRpc;
import lombok.extern.log4j.Log4j2;

import java.util.concurrent.TimeUnit;

@Log4j2
public class MsgPollWorker implements Runnable {
    private static final long IO_LOOP_WAIT = 20;
    private static final long IO_TIMEOUT = 20;
    private final Object ioLock = new Object[0];
    private volatile boolean stop = false;

    private final RaftServer server;

    public MsgPollWorker(RaftServer server) {
        this.server = server;
    }

    public void stop() {
        synchronized (ioLock) {
            stop = true;
            ioLock.notifyAll();
        }
    }

    @Override
    public void run() {
        try {
            while (!stop) {
                synchronized (ioLock) {
                    ioLock.wait(IO_LOOP_WAIT);
                }
                BaseRpc message = server.getNetwork().pollMsg(server.getId(), IO_TIMEOUT, TimeUnit.MILLISECONDS);
                if (message != null) {
                    server.visit(message);
                }
            }
        } catch (InterruptedException e) {
            log.error(e.getMessage(), e);
            Thread.currentThread().interrupt();
        }
    }
}
