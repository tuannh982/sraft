package com.tuannh.sraft.utility.timer;

import com.tuannh.sraft.commons.observer.Listener;
import com.tuannh.sraft.commons.observer.Notifier;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

import java.util.List;

@Log4j2
public final class DefaultTimer implements Timer, Notifier<Long>, Runnable {
    // --------------------------------------------------------
    @Getter
    private Thread thread;
    private final List<Listener<Long>> listeners;
    // --------------------------------------------------------
    @Getter
    private long timeout = 0;
    private final Object waitLock = new Object[0];
    private volatile boolean reset = false;
    private volatile boolean stop = false;

    private DefaultTimer(List<Listener<Long>> listeners) {
        this.listeners = listeners;
    }

    public static DefaultTimer create(String name, long timeout, List<Listener<Long>> listeners) {
        DefaultTimer timer = new DefaultTimer(listeners);
        timer.timeout = timeout;
        Thread timerThread = new Thread(timer);
        timer.thread = timerThread;
        timerThread.setName(name);
        timerThread.start();
        return timer;
    }

    @Override
    public List<Listener<Long>> getListeners() {
        return listeners;
    }

    @Override
    public void reset(long timeout) {
        this.timeout = timeout;
        reset();
    }

    @Override
    public void reset() {
        synchronized (waitLock) {
            reset = true;
            waitLock.notifyAll();
        }
    }

    @Override
    public void stop() {
        stop = true;
        reset();
        try {
            thread.join();
        } catch (InterruptedException e) {
            log.error(e.getMessage(), e);
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public void run() {
        while (!stop) {
            sleep();
            if (stop) {
                break;
            }
            if (reset) continue;
            notifyTimeout();
        }
    }

    @SuppressWarnings("java:S135")
    private void sleep() {
        try {
            while (!stop) {
                reset = false;
                synchronized (waitLock) {
                    waitLock.wait(timeout);
                }
                if (reset) {
                    continue;
                }
                break;
            }
        } catch (InterruptedException e) {
            log.error(e.getMessage(), e);
            Thread.currentThread().interrupt();
        }
    }

    private void notifyTimeout() {
        this.notifyEvent(System.currentTimeMillis());
    }
}
