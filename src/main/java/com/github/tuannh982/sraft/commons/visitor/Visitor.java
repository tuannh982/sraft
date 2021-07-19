package com.github.tuannh982.sraft.commons.visitor;

public interface Visitor<T, R> {
    R visit(T o);
}
