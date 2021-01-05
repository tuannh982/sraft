package com.tuannh.sraft.network;

import lombok.NonNull;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class SimpleQueueBasedNetwork<I extends Serializable> implements Network<I> {
    private final Map<I, BlockingQueue<Packet<I, ?>>> queue = new HashMap<>();
    private final Set<I> clients = new HashSet<>();

    @Override
    public void register(I id) {
        if (!queue.containsKey(id)) {
            queue.put(id, new LinkedBlockingQueue<>());
            clients.add(id);
        }
    }

    @Override
    public void deregister(I id) {
        queue.remove(id);
        clients.remove(id);
    }

    @Override
    public <T extends Serializable> void broadcast(@NonNull I from, T message) {
        for (I to : clients) {
            if (!Objects.equals(to, from)) {
                queue.get(to).add(new Packet<>(from, to, message));
            }
        }
    }

    @Override
    public <T extends Serializable> void sendMsg(@NonNull I from, @NonNull I to, T message) {
        if (Objects.equals(from, to)) {
            return;
        }
        if (!queue.containsKey(to)) {
            return;
        }
        queue.get(to).add(new Packet<>(from, to, message));
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends Serializable> T pollMsg(I id, long timeout, TimeUnit unit) throws InterruptedException {
        if (!queue.containsKey(id)) {
            return null;
        }
        Packet<I, ?> packet = queue.get(id).poll(timeout, unit);
        if (packet == null) return null;
        try {
            return (T) packet.getPayload();
        } catch (ClassCastException e) {
            // ignored
        }
        return null;
    }
}
