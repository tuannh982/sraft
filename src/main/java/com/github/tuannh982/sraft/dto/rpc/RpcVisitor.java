package com.github.tuannh982.sraft.dto.rpc;

import com.github.tuannh982.sraft.commons.visitor.Visitor;

@SuppressWarnings("java:S1905")
public interface RpcVisitor extends Visitor<BaseRpc, Boolean> {
    @Override
    default Boolean visit(BaseRpc o) {
        boolean b = preHandle(o);
        if (!b) return false;
        if (o instanceof AppendEntries) {
            b = handle((AppendEntries)o);
        } else if (o instanceof AppendEntriesResponse) {
            b = handle((AppendEntriesResponse)o);
        } else if (o instanceof RequestVote) {
            b = handle((RequestVote)o);
        } else if (o instanceof RequestVoteResponse) {
            b = handle((RequestVoteResponse)o);
        }
        if (!b) return false;
        postHandle(o);
        return true;
    }

    default boolean preHandle(BaseRpc o) {
        return true;
    }
    default void postHandle(BaseRpc o) {}
    boolean handle(AppendEntries message);
    boolean handle(AppendEntriesResponse message);
    boolean handle(RequestVote message);
    boolean handle(RequestVoteResponse message);
}
