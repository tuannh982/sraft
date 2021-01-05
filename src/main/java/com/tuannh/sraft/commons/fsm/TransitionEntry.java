package com.tuannh.sraft.commons.fsm;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public final class TransitionEntry<T extends FsmEntity> {
    private final State before;
    private final Event event;
    private final State after;
    private final Handler<T> handler;

    public TransitionEntry(State before, Event event, State after) {
        this.before = before;
        this.event = event;
        this.after = after;
        this.handler = entity -> entity.changeState(after);
    }

    public interface Handler<E extends FsmEntity> {
        void handle(E entity);
        default Handler<E> next() {
            return null;
        }
    }

    public final void handle(T entity) {
        Handler<T> currentHandle = handler;
        while (currentHandle != null) {
            currentHandle.handle(entity);
            currentHandle = currentHandle.next();
        }
    }
}
