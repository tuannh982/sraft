package com.github.tuannh982.sraft.commons.fsm;

public interface FsmEntity {
    State state();
    void changeState(State newState);
}
