package com.tuannh.sraft.commons.fsm;

import com.tuannh.sraft.commons.tuple.Tuple2;
import com.tuannh.sraft.commons.tuple.Tuple3;
import com.tuannh.sraft.commons.tuple.TupleFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FSM<T extends FsmEntity> {
    private final Map<Tuple2<State, Event>, State> transitionTable;
    private final Map<Tuple3<State, Event, State>, TransitionEntry<T>> executors;

    public FSM(List<TransitionEntry<T>> entries) {
        transitionTable = new HashMap<>();
        executors = new HashMap<>();
        for (TransitionEntry<T> entry : entries) {
            transitionTable.put(TupleFactory.of(entry.getBefore(), entry.getEvent()), entry.getAfter());
            executors.put(TupleFactory.of(entry.getBefore(), entry.getEvent(), entry.getAfter()), entry);
        }
    }

    public final void transition(T entity, Event event) {
        State newState = transitionTable.get(TupleFactory.of(entity.state(), event));
        if (newState == null) {
            return; // state remain unchanged
        }
        TransitionEntry<T> entry = executors.get(TupleFactory.of(entity.state(), event, newState));
        entity.changeState(newState);
        entry.handle(entity);
    }
}
