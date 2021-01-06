package com.tuannh.sraft.utility.network;

import lombok.NonNull;
import lombok.extern.log4j.Log4j2;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

@Log4j2
public final class SimpleQueueBasedNetwork<I> implements Network<I> {
    private final Map<I, BlockingQueue<Packet<I, ?>>> queue = new HashMap<>();
    private final Set<I> clients = new HashSet<>();

    @Override
    public void register(I id) {
        log.debug("{} joined", id);
        if (!queue.containsKey(id)) {
            queue.put(id, new LinkedBlockingQueue<>());
            clients.add(id);
        }
    }

    @Override
    public void deregister(I id) {
        log.debug("{} left", id);
        queue.remove(id);
        clients.remove(id);
    }

    @Override
    public <T> void broadcast(@NonNull I from, T message) {
        log.debug("{} broadcast message {} to network", from, message);
        for (I to : clients) {
            if (!Objects.equals(to, from)) {
                queue.get(to).add(new Packet<>(from, to, message));
            }
        }
    }

    @Override
    public <T> void sendMsg(@NonNull I from, @NonNull I to, T message) {
        if (Objects.equals(from, to)) {
            return;
        }
        if (!queue.containsKey(to)) {
            return;
        }
        log.debug("{} send message {} to {}", from, message, to);
        queue.get(to).add(new Packet<>(from, to, message));
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T pollMsg(I id, long timeout, TimeUnit unit) throws InterruptedException {
        if (!queue.containsKey(id)) {
            return null;
        }
        Packet<I, ?> packet = queue.get(id).poll(timeout, unit);
        if (packet == null) return null;
        log.debug("{} receive packet {}", id, packet);
        try {
            return (T) packet.getPayload();
        } catch (ClassCastException e) {
            // ignored
        }
        return null;
    }
}
