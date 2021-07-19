package com.github.tuannh982.sraft.commons.fsm;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode
@Getter
@AllArgsConstructor
public final class Event {
    private final String value;

    public static Event of(String name) {
        return new Event(name);
    }
}
