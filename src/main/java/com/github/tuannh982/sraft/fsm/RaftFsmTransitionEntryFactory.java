package com.github.tuannh982.sraft.fsm;

import com.github.tuannh982.sraft.commons.fsm.TransitionEntry;
import com.github.tuannh982.sraft.fsm.event.FsmEvent;
import com.github.tuannh982.sraft.fsm.state.FsmState;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class RaftFsmTransitionEntryFactory {
    public static TransitionEntry<RaftFsm> create(FsmState oldState, FsmEvent event, FsmState newState, TransitionEntry.Handler<RaftFsm> handler) {
        return new TransitionEntry<>(oldState.getValue(), event.getValue(), newState.getValue(), handler);
    }
}
