# Simple RAFT implementation

# Introduction

Consensus algorithm implementation (RAFT)

from paper https://raft.github.io/raft.pdf

demonstration http://thesecretlivesofdata.com/raft/

# Quickstarts

run `main` in class `Test`

# Features

- [x] leader election
- [x] logs replication
- [ ] membership change
- [ ] log compaction

**Note #1:** this project only for educational purpose, those features will not be optimized for production

**Note #2:** `membership change` and `log compaction` are not implemented since we only need `leader election`
and `logs replication`

# TODOs

- [x] network emulation
    - [x] join/leave network
    - [x] transport (send, recv, broadcast)
    - [x] message encapsulation
    - [x] async IO (simple message queue)
    - [x] network jamming
    - [x] test
- [x] timer
    - [x] scheduled task
    - [x] listen/notify
    - [x] change timeout
    - [x] test
- [x] raft
    - [x] rpc
        - [x] AppendEntries
        - [x] AppendEntries response (custom)
        - [x] RequestVote
        - [x] RequestVote response
        - [x] test
    - [x] client command (deposit, withdraw)
        - [x] test
    - [ ] states
        - [ ] leader
            - [x] heartbeat
            - [x] send logs
            - [ ] test
        - [x] voter
            - [x] handle RequestVote message
            - [x] test
        - [x] candidate
            - [x] broadcast RequestVote 
            - [x] collect votes
            - [x] test
        - [ ] follower
            - [x] handle AppendEntries message
            - [ ] test
    - [x] FSM
        - [x] transition table
        - [x] state change listener
        - [x] test
    - [x] raft log
        - [x] test
    