package com.tuannh.sraft.fsm.state;

import com.tuannh.sraft.commons.fsm.State;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FsmState {
    LEADER(State.of("leader")),
    CANDIDATE(State.of("candidate")),
    FOLLOWER(State.of("follower"));

    private final State value;
}
