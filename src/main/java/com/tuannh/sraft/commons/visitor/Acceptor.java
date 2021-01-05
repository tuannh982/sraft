package com.tuannh.sraft.commons.visitor;

public interface Acceptor<T, R> {
    R accept(Visitor<T, R> visitor);
}
