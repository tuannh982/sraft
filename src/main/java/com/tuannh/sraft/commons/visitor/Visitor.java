package com.tuannh.sraft.commons.visitor;

public interface Visitor<T, R> {
    R visit(T o);
}
