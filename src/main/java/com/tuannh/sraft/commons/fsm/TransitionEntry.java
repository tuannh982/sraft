package com.tuannh.sraft.commons.fsm;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public final class TransitionEntry {
    private final State before;
    private final Event event;
    private final State after;
    private final Handler<FsmEntity> handler;

    public interface Handler<E> {
        void handle(E entity);
        default Handler<E> next() {
            return null;
        }
    }

    public final void handle(FsmEntity entity) {
        Handler<FsmEntity> currentHandle = handler;
        while (currentHandle != null) {
            currentHandle.handle(entity);
            currentHandle = currentHandle.next();
        }
    }
}
