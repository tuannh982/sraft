package com.tuannh.sraft.commons.observer;

import java.util.List;

public interface Notifier<T> {
    List<Listener<T>> getListeners();

    default void notifyEvent(T event) {
        for (Listener<T> listener : getListeners()) {
            listener.onEvent(event);
        }
    }
}