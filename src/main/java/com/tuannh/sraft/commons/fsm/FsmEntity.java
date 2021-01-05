package com.tuannh.sraft.commons.fsm;

public interface FsmEntity {
    State state();
    void changeState(State newState);
}
