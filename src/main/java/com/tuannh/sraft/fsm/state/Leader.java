package com.tuannh.sraft.fsm.state;

import com.tuannh.sraft.dto.rpc.AppendEntries;
import com.tuannh.sraft.dto.rpc.AppendEntriesResponse;
import com.tuannh.sraft.dto.rpc.RequestVote;
import com.tuannh.sraft.dto.rpc.RequestVoteResponse;
import com.tuannh.sraft.fsm.RaftFsm;
import com.tuannh.sraft.log.RaftLog;
import com.tuannh.sraft.server.RaftData;
import com.tuannh.sraft.server.RaftServer;
import lombok.extern.log4j.Log4j2;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Log4j2
public class Leader extends ServerState {
    private final Map<String, AtomicLong> nextIndex = new HashMap<>();
    private final Map<String, AtomicLong> matchIndex = new HashMap<>();

    public Leader(RaftServer server, RaftFsm fsm, RaftData data) {
        super(server, fsm, data);
        state = FsmState.LEADER;
        server.getTimer().reset(server.getHeartbeatTimeout());
        sendHeartBeat(server.getId());
        for (String neighbor : server.getNeighbors()) {
            nextIndex.put(neighbor, new AtomicLong(data.getLastLogIndex().get() + 1));
            matchIndex.put(neighbor, new AtomicLong(0));
        }
    }

    public synchronized void heartbeat() {
        sendHeartBeat(server.getId());
    }

    private synchronized void sendHeartBeat(String serverId) {
        log.info("{} | server send heartbeat", server.getId() + state);
        server.getNetwork().broadcast(serverId, new AppendEntries(
                serverId,
                data.getCurrentTerm().get(),
                serverId,
                data.getLastLogIndex().get(),
                data.getLastLogTerm().get(),
                new RaftLog[0],
                data.getCommitIndex().get()
        ));
    }

    @Override
    public boolean handle(AppendEntries message) {
        // NOOP
        return false;
    }

    @Override
    public synchronized boolean handle(AppendEntriesResponse message) {
        if (message.isSuccess()) {
            nextIndex.get(message.getFrom()).incrementAndGet();
            if (nextIndex.get(message.getFrom()).get() > data.getLastLogIndex().get()) {
                nextIndex.get(message.getFrom()).set(data.getLastLogIndex().get());
            }
            return true;
        } else {
            final String serverId = server.getId();
            nextIndex.get(message.getFrom()).decrementAndGet();
            final long prevIndex = Math.max(0, nextIndex.get(message.getFrom()).get() - 1);
            RaftLog prevLog = data.getLogs().get((int) prevIndex); // FIXME
            RaftLog currentLog = data.getLogs().get((int) nextIndex.get(message.getFrom()).get()); // FIXME
            AppendEntries msg = new AppendEntries(
                    serverId,
                    data.getCurrentTerm().get(),
                    serverId,
                    prevIndex,
                    prevLog.getTerm(),
                    new RaftLog[] {currentLog},
                    data.getCommitIndex().get()
            );
            log.info("{} | server send heartbeat with data {}", server.getId() + state, msg);
            server.getNetwork().sendMsg(serverId, message.getFrom(), msg);
            return true;
        }
    }

    @Override
    public boolean handle(RequestVote message) {
        // NOOP
        return false;
    }

    @Override
    public boolean handle(RequestVoteResponse message) {
        // NOOP
        return false;
    }
}
