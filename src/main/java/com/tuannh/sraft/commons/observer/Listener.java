package com.tuannh.sraft.commons.observer;

public interface Listener<T> {
    void onEvent(T event);
}
