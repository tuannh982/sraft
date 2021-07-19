package com.github.tuannh982.sraft.commons.fsm;

import lombok.AllArgsConstructor;
import lombok.ToString;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class FsmTest {
    @ToString
    @AllArgsConstructor
    static class Order implements FsmEntity {
        private final String name;
        private State state;

        @Override
        public State state() {
            return state;
        }

        @Override
        public void changeState(State newState) {
            state = newState;
        }
    }

    @Test
    public void simpleTest() {
        // declare states
        State newOrder = State.of("new-order");
        State processingOrder = State.of("processing-order");
        State doneOrder = State.of("done-order");
        // declare events
        Event process = Event.of("process");
        Event done = Event.of("done");
        // entries
        TransitionEntry<Order> new2processing = new TransitionEntry<>(
                newOrder,
                process,
                processingOrder,
                entity -> System.out.println(entity + " new2processing called")
        );
        TransitionEntry<Order> processing2done = new TransitionEntry<>(
                processingOrder,
                done,
                doneOrder,
                entity -> System.out.println(entity + " processing2done called")
        );
        FSM<Order> fsm = new FSM<>(
                Arrays.asList(new2processing, processing2done)
        );
        Order x = new Order("order1", newOrder);
        fsm.transition(x, process);
        assertEquals(x.state(), processingOrder);
        fsm.transition(x, done);
        assertEquals(x.state(), doneOrder);
    }
}
