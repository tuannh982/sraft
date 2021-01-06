package com.tuannh.sraft.fsm;

import com.tuannh.sraft.commons.fsm.TransitionEntry;
import com.tuannh.sraft.fsm.event.FsmEvent;
import com.tuannh.sraft.fsm.state.FsmState;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class RaftFsmTransitionEntryFactory {
    public static TransitionEntry<RaftFsm> create(FsmState oldState, FsmEvent event, FsmState newState, TransitionEntry.Handler<RaftFsm> handler) {
        return new TransitionEntry<>(oldState.getValue(), event.getValue(), newState.getValue(), handler);
    }
}
