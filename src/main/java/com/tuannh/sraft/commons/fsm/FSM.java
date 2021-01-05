package com.tuannh.sraft.commons.fsm;

import com.tuannh.sraft.commons.tuple.Tuple2;
import com.tuannh.sraft.commons.tuple.Tuple3;
import com.tuannh.sraft.commons.tuple.TupleFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FSM {
    private final Map<Tuple2<State, Event>, State> transitionTable;
    private final Map<Tuple3<State, Event, State>, TransitionEntry> executors;

    public FSM(List<TransitionEntry> entries) {
        transitionTable = new HashMap<>();
        executors = new HashMap<>();
        for (TransitionEntry entry : entries) {
            transitionTable.put(TupleFactory.of(entry.getBefore(), entry.getEvent()), entry.getAfter());
            executors.put(TupleFactory.of(entry.getBefore(), entry.getEvent(), entry.getAfter()), entry);
        }
    }

    public void transition(FsmEntity entity, Event event) {
        State newState = transitionTable.get(TupleFactory.of(entity.state(), event));
        if (newState == null) {
            throw new IllegalStateException();
        }
        TransitionEntry entry = executors.get(TupleFactory.of(entity.state(), event, newState));
        entity.changeState(newState);
        entry.handle(entity);
    }
}
