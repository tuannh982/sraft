package com.tuannh.sraft.fsm.event;

import com.tuannh.sraft.commons.fsm.Event;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FsmEvent {
    TIMEOUT(Event.of("timer-timeout")),
    MAJORITY_VOTE_RECEIVED(Event.of("majority-vote-received")),
    LEADER_DISCOVERED(Event.of("leader-discovered")),
    HIGHER_TERM_DISCOVERED(Event.of("higher-term-discovered"));

    private final Event value;
}
