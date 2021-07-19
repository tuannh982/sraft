package com.github.tuannh982.sraft.commons.observer;

public interface Listener<T> {
    void onEvent(T event);
}
