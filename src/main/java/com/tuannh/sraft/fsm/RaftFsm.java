package com.tuannh.sraft.fsm;

import com.tuannh.sraft.commons.fsm.*;
import com.tuannh.sraft.dto.rpc.*;
import com.tuannh.sraft.fsm.event.FsmEvent;
import com.tuannh.sraft.fsm.state.FsmState;
import com.tuannh.sraft.fsm.state.ServerState;
import com.tuannh.sraft.server.RaftData;
import com.tuannh.sraft.server.RaftServer;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Log4j2
public final class RaftFsm extends FSM<RaftFsm> implements FsmEntity {
    private static final List<TransitionEntry<RaftFsm>> entries;
    static {
        List<TransitionEntry<RaftFsm>> tEntries = new ArrayList<>();
        tEntries.add(RaftFsmTransitionEntryFactory.create(
                FsmState.FOLLOWER, FsmEvent.TIMEOUT, FsmState.CANDIDATE,
                null
        ));
        tEntries.add(RaftFsmTransitionEntryFactory.create(
                FsmState.CANDIDATE, FsmEvent.MAJORITY_VOTE_RECEIVED, FsmState.LEADER,
                null
        ));
        tEntries.add(RaftFsmTransitionEntryFactory.create(
                FsmState.CANDIDATE, FsmEvent.LEADER_DISCOVERED, FsmState.FOLLOWER,
                null
        ));
        tEntries.add(RaftFsmTransitionEntryFactory.create(
                FsmState.CANDIDATE, FsmEvent.HIGHER_TERM_DISCOVERED, FsmState.FOLLOWER,
                null
        ));
        tEntries.add(RaftFsmTransitionEntryFactory.create(
                FsmState.LEADER, FsmEvent.HIGHER_TERM_DISCOVERED, FsmState.FOLLOWER,
                null
        ));
        entries = Collections.unmodifiableList(tEntries);
    }

    //
    @Getter
    private RaftServer server;
    @Getter
    private ServerState state;
    //
    private final Thread msgPollThread;

    public RaftFsm(RaftServer server) {
        super(entries);
        this.server = server;
        msgPollThread = new Thread(new MessagePollingWorker(server));
        msgPollThread.setName("message-polling-thread-" + server.getId());
    }

    @Override
    public State state() {
        return state.getState().getValue();
    }

    @Override
    public void changeState(State newState) {
        FsmState newFState = FsmState.from(newState);
        switch (newFState) {
            case LEADER:
                break;
            case FOLLOWER:
                break;
            case CANDIDATE:
                break;
        }
    }

    public final void transition(FsmEvent event) {
        transition(this, event.getValue());
    }

    private static class MessagePollingWorker implements Runnable {
        private static final long IO_LOOP_WAIT = 50;
        private static final long IO_TIMEOUT = 50;
        private final Object ioLock = new Object[0];
        private volatile boolean stop = false;

        private final RaftServer server;

        public MessagePollingWorker(RaftServer server) {
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
                    server.visit(message);
                }
            } catch (InterruptedException e) {
                log.error(e.getMessage(), e);
                Thread.currentThread().interrupt();
            }
        }
    }
}
