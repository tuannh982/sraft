package com.github.tuannh982.sraft.commons.visitor;

public interface Acceptor<T, R> {
    R accept(Visitor<T, R> visitor);
}
