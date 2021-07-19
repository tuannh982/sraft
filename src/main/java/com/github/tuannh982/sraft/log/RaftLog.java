package com.github.tuannh982.sraft.log;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
@AllArgsConstructor
public class RaftLog {
    private final String command;
    private final long term;
}
