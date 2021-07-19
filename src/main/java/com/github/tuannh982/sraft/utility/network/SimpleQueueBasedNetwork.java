package com.github.tuannh982.sraft.utility.network;

import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

@Log4j2
public final class SimpleQueueBasedNetwork<I> implements Network<I> {
    private final Map<I, BlockingQueue<Packet<I, ?>>> queue = new HashMap<>();
    private final Set<I> clients = new HashSet<>();
    private final Map<Pair<I, I>, Set<JammingEntry>> jammingMap = new HashMap<>();
    private final Random random = new Random(System.currentTimeMillis());

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
        jammingMap.keySet().removeIf(entry -> Objects.equals(entry.getRight(), id));
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

    @SuppressWarnings({"java:S1199", "java:S2273", "java:S3776", "java:S2274", "SynchronizationOnLocalVariableOrMethodParameter"})
    private Packet<I, ?> applyJam(final Packet<I, ?> packet) throws InterruptedException {
        Object monitor = new Object[0];
        synchronized (monitor) {
            I from = packet.getSender();
            I to = packet.getReceiver();
            Packet<I, ?> retPacket = packet;
            if (!jammingMap.containsKey(ImmutablePair.of(from, to))) {
                return retPacket;
            } else {
                Set<JammingEntry> jammingSet = jammingMap.get(ImmutablePair.of(from, to));
                if (jammingSet == null || jammingSet.isEmpty()) {
                    return packet;
                } else {
                    for (JammingEntry entry : jammingSet) {
                        switch (entry.getType()) {
                            case LATENCY: {
                                monitor.wait((long) entry.getValue());
                                break;
                            }
                            case LOSS: {
                                double chance = random.nextDouble();
                                if (chance < entry.getValue()) {
                                    // drop packet
                                    retPacket = null;
                                }
                                break;
                            }
                            default:
                                retPacket = null;
                        }
                    }
                    return retPacket;
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T pollMsg(I id) throws InterruptedException {
        if (!queue.containsKey(id)) {
            return null;
        }
        Packet<I, ?> packet = queue.get(id).poll(50, TimeUnit.MILLISECONDS);
        if (packet == null) return null;
        Packet<I, ?> cPacket = applyJam(packet);
        if (cPacket == null) return null;
        log.debug("{} receive packet {}", id, cPacket);
        try {
            return (T) cPacket.getPayload();
        } catch (ClassCastException e) {
            // ignored
        }
        return null;
    }

    @Override
    public void jamConnection(JammingType jammingType, double value, I from, I to) {
        if (!jammingMap.containsKey(ImmutablePair.of(from, to))) {
            jammingMap.put(ImmutablePair.of(from, to), new HashSet<>());
        }
        jammingMap.get(ImmutablePair.of(from, to)).add(new JammingEntry(jammingType, value));
    }

    @Override
    public void clearJam(I from, I to) {
        jammingMap.put(ImmutablePair.of(from, to), new HashSet<>());
    }
}
