package com.tuannh.sraft.commons.tuple;

public abstract interface Tuple {
    Object[] toArray();
    Object get(int index);
}
