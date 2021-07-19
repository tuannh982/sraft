package com.github.tuannh982.sraft.fsm.state;

import com.github.tuannh982.sraft.commons.fsm.State;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Getter
@AllArgsConstructor
public enum FsmState {
    LEADER(State.of("leader")),
    CANDIDATE(State.of("candidate")),
    FOLLOWER(State.of("follower"));

    private final State value;

    private static final Map<State, FsmState> mx;
    static {
        Map<State, FsmState> mxx = new HashMap<>();
        for (FsmState s : FsmState.values()) {
            mxx.put(s.getValue(), s);
        }
        mx = Collections.unmodifiableMap(mxx);
    }

    public static FsmState from(State s) {
        FsmState ret = mx.get(s);
        if (ret == null) throw new IllegalStateException("state not defined");
        return ret;
    }
}
