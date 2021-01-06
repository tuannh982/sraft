package com.tuannh.sraft.fsm;

import com.tuannh.sraft.commons.fsm.*;
import com.tuannh.sraft.dto.rpc.*;
import com.tuannh.sraft.fsm.event.FsmEvent;
import com.tuannh.sraft.fsm.state.*;
import com.tuannh.sraft.server.RaftServer;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Log4j2
public final class RaftFsm extends FSM<RaftFsm> implements FsmEntity {
    private static final List<TransitionEntry<RaftFsm>> entries;
    static {
        List<TransitionEntry<RaftFsm>> tEntries = new ArrayList<>();
        tEntries.add(RaftFsmTransitionEntryFactory.create(
                FsmState.FOLLOWER, FsmEvent.TIMEOUT, FsmState.CANDIDATE, null
        ));
        tEntries.add(RaftFsmTransitionEntryFactory.create(
                FsmState.CANDIDATE, FsmEvent.TIMEOUT, FsmState.CANDIDATE, null
        ));
        tEntries.add(RaftFsmTransitionEntryFactory.create(
                FsmState.CANDIDATE, FsmEvent.MAJORITY_VOTE_RECEIVED, FsmState.LEADER, null
        ));
        tEntries.add(RaftFsmTransitionEntryFactory.create(
                FsmState.FOLLOWER, FsmEvent.HIGHER_TERM_DISCOVERED, FsmState.FOLLOWER, null
        ));
        tEntries.add(RaftFsmTransitionEntryFactory.create(
                FsmState.CANDIDATE, FsmEvent.HIGHER_TERM_DISCOVERED, FsmState.FOLLOWER, null
        ));
        tEntries.add(RaftFsmTransitionEntryFactory.create(
                FsmState.LEADER, FsmEvent.HIGHER_TERM_DISCOVERED, FsmState.FOLLOWER, null
        ));
        tEntries.add(RaftFsmTransitionEntryFactory.create(
                FsmState.LEADER, FsmEvent.TIMEOUT, FsmState.LEADER, null
        ));
        entries = Collections.unmodifiableList(tEntries);
    }

    //
    @Getter
    private RaftServer server;
    @Getter
    private ServerState state;

    public RaftFsm(RaftServer server) {
        super(entries);
        this.server = server;
        this.state = new Follower(server, this, server.getData());
    }

    @Override
    public State state() {
        return state.getState().getValue();
    }

    @Override
    public synchronized void changeState(State newState) {
        FsmState oldFState = state.getState();
        FsmState newFState = FsmState.from(newState);
        log.info("{} | state changed from {} to {}", server.getId() + state.getState(), oldFState, newFState);
        server.getTimer().reset();
        log.info("{} | timer reset", server.getId() + state.getState());
        if (oldFState != newFState) {
            switch (newFState) {
                case LEADER:
                    state = new Leader(server, this, server.getData());
                    break;
                case FOLLOWER:
                    state = new Follower(server, this, server.getData());
                    break;
                case CANDIDATE:
                    state = new Candidate(server, this, server.getData());
                    break;
            }
        } else {
            switch (newFState) {
                case FOLLOWER:
                case CANDIDATE:
                    break;
                case LEADER:
                    ((Leader)state).heartbeat();
                    break;
            }
        }
    }

    public final synchronized void transition(FsmEvent event) {
        log.info("{} | event {}", server.getId() + state.getState(), event);
        transition(this, event.getValue());
    }
}
