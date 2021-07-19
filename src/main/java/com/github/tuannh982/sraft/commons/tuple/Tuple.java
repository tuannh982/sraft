package com.github.tuannh982.sraft.commons.tuple;

public abstract interface Tuple {
    Object[] toArray();
    Object get(int index);
}
