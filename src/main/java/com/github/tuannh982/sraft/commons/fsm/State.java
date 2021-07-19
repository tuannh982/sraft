package com.github.tuannh982.sraft.commons.fsm;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode
@Getter
@AllArgsConstructor
public final class State {
    private final String value;

    public static State of(String name) {
        return new State(name);
    }
}
