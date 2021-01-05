package com.tuannh.sraft.fsm.handler;

import com.tuannh.sraft.commons.fsm.TransitionEntry;
import com.tuannh.sraft.fsm.RaftFsm;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class ElectionTimeout implements TransitionEntry.Handler<RaftFsm> {
    @Override
    public void handle(RaftFsm fsm) {
        log.info("election timeout");
    }
}
