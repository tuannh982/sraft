package com.github.tuannh982.sraft.fsm.event;

import com.github.tuannh982.sraft.commons.fsm.Event;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Getter
@AllArgsConstructor
public enum FsmEvent {
    TIMEOUT(Event.of("timer-timeout")),
    MAJORITY_VOTE_RECEIVED(Event.of("majority-vote-received")),
    HIGHER_TERM_DISCOVERED(Event.of("higher-term-discovered"));

    private final Event value;

    private static final Map<Event, FsmEvent> mx;
    static {
        Map<Event, FsmEvent> mxx = new HashMap<>();
        for (FsmEvent s : FsmEvent.values()) {
            mxx.put(s.getValue(), s);
        }
        mx = Collections.unmodifiableMap(mxx);
    }

    public static FsmEvent from(Event s) {
        FsmEvent ret = mx.get(s);
        if (ret == null) throw new IllegalStateException("event not defined");
        return ret;
    }
}
