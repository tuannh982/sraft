package com.tuannh.sraft.dto.rpc;

import com.tuannh.sraft.commons.visitor.Visitor;

@SuppressWarnings("java:S1905")
public interface RpcVisitor extends Visitor<BaseRpc, Void> {
    @Override
    default Void visit(BaseRpc o) {
        preHandle(o);
        if (o instanceof AppendEntries) {
            handle((AppendEntries)o);
        } else if (o instanceof AppendEntriesResponse) {
            handle((AppendEntriesResponse)o);
        } else if (o instanceof RequestVote) {
            handle((RequestVote)o);
        } else if (o instanceof RequestVoteResponse) {
            handle((RequestVoteResponse)o);
        }
        postHandle(o);
        return null;
    }

    default void preHandle(BaseRpc o) {}
    default void postHandle(BaseRpc o) {}
    void handle(AppendEntries message);
    void handle(AppendEntriesResponse message);
    void handle(RequestVote message);
    void handle(RequestVoteResponse message);
}
