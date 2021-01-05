package com.tuannh.sraft.fsm;

import com.tuannh.sraft.commons.fsm.FSM;
import com.tuannh.sraft.commons.fsm.FsmEntity;
import com.tuannh.sraft.commons.fsm.State;
import com.tuannh.sraft.commons.fsm.TransitionEntry;
import com.tuannh.sraft.fsm.event.FsmEvent;
import com.tuannh.sraft.fsm.handler.ElectionTimeout;
import com.tuannh.sraft.fsm.state.FsmState;
import com.tuannh.sraft.fsm.state.ServerState;

import java.util.ArrayList;
import java.util.List;

public class RaftFsm extends FSM<RaftFsm> implements FsmEntity {
    private static final List<TransitionEntry<RaftFsm>> entries;
    static {
        entries = new ArrayList<>();
        entries.add(RaftFsmTransitionEntryFactory.create(
                FsmState.FOLLOWER, FsmEvent.TIMEOUT, FsmState.CANDIDATE,
                new ElectionTimeout()
        ));
        entries.add(RaftFsmTransitionEntryFactory.create(
                FsmState.CANDIDATE, FsmEvent.MAJORITY_VOTE_RECEIVED, FsmState.LEADER,
                null
        ));
        entries.add(RaftFsmTransitionEntryFactory.create(
                FsmState.CANDIDATE, FsmEvent.LEADER_DISCOVERED, FsmState.FOLLOWER,
                null
        ));
        entries.add(RaftFsmTransitionEntryFactory.create(
                FsmState.CANDIDATE, FsmEvent.HIGHER_TERM_DISCOVERED, FsmState.FOLLOWER,
                null
        ));
        entries.add(RaftFsmTransitionEntryFactory.create(
                FsmState.LEADER, FsmEvent.HIGHER_TERM_DISCOVERED, FsmState.FOLLOWER,
                null
        ));
    } // FIXME

    private ServerState state;

    public RaftFsm() {
        super(entries);
    }

    @Override
    public State state() {
        return state.getState().getValue();
    }

    @Override
    public void changeState(State newState) {

    }
}
