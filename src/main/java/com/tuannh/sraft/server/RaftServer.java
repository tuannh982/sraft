package com.tuannh.sraft.server;

import com.tuannh.sraft.commons.observer.Listener;
import com.tuannh.sraft.commons.rand.Random;
import com.tuannh.sraft.dto.cmd.ClientCommand;
import com.tuannh.sraft.dto.cmd.ClientCommandVisitor;
import com.tuannh.sraft.dto.rpc.*;
import com.tuannh.sraft.fsm.RaftFsm;
import com.tuannh.sraft.fsm.event.FsmEvent;
import com.tuannh.sraft.utility.network.Network;
import com.tuannh.sraft.utility.timer.DefaultTimer;
import com.tuannh.sraft.utility.timer.Timer;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Log4j2
@Getter
public class RaftServer implements RpcVisitor, Listener<Long> {
    // server
    private final String id;
    private final int quorumSize;
    private final List<String> quorum;
    private final List<String> neighbors;
    private final int minimalVoteCount;
    // timer
    private final Timer timer;
    private final long electionTimeout;
    private final long heartbeatTimeout;
    private final long overhead;
    // data
    private final Network<String> network;
    private final RaftFsm fsm;
    private final RaftData data;
    //
    private final Thread msgPollThread;

    public RaftServer(String id, List<String> quorum, Network<String> network) {
        // identity
        this.id = id;
        // quorum
        this.quorum = quorum;
        quorumSize = quorum.size();
        minimalVoteCount = (quorumSize + 1) / 2;
        neighbors = new ArrayList<>();
        for (String s : quorum) {
            if (StringUtils.equals(s, id)) continue;
            neighbors.add(s);
        }
        // network
        this.network = network;
        network.register(id);
        // timer
        electionTimeout = 500;
        heartbeatTimeout = 100;
        overhead = 100;
        final long initialTimeout = Random.rand(electionTimeout, overhead);
        log.info("{} | initial timeout {}", id, initialTimeout);
        timer = DefaultTimer.create("timer-" + id, initialTimeout, Collections.singletonList(this));
        // msg polling thread
        msgPollThread = new Thread(new MsgPollWorker(this));
        msgPollThread.setName("message-polling-thread-" + id);
        msgPollThread.start();
        // internal
        data = new RaftData();
        fsm = new RaftFsm(this);
    }

    public void stop() {
        timer.stop();
        network.deregister(id);
    }

    // handle message

    @Override
    public boolean preHandle(BaseRpc message) {
        long currentTerm = data.getCurrentTerm().get();
        if (message.getTerm() > currentTerm) {
            log.info("{} | detected higher term, term = {}, current_term = {}", id + fsm.getState().getState(), message.getTerm(), currentTerm);
            data.getCurrentTerm().set(message.getTerm());
            fsm.transition(FsmEvent.HIGHER_TERM_DISCOVERED);
            return true;
        } else if (message.getTerm() < currentTerm) {
            if (message instanceof AppendEntries) {
                network.sendMsg(id, message.getFrom(), new AppendEntriesResponse(
                        id,
                        currentTerm,
                        false
                ));
            } else if (message instanceof RequestVote) {
                network.sendMsg(id, message.getFrom(), new RequestVoteResponse(
                        id,
                        currentTerm,
                        false
                ));
            }
            return false;
        }
        return true;
    }

    @Override
    public boolean handle(AppendEntries message) {
        return fsm.getState().visit(message);
    }

    @Override
    public boolean handle(AppendEntriesResponse message) {
        return fsm.getState().visit(message);
    }

    @Override
    public boolean handle(RequestVote message) {
        return fsm.getState().visit(message);
    }

    @Override
    public boolean handle(RequestVoteResponse message) {
        return fsm.getState().visit(message);
    }

    // timeout handle

    @Override
    public void onEvent(Long event) {
        fsm.transition(FsmEvent.TIMEOUT);
    }
}
