package com.tuannh.sraft.dto.rpc;

import com.tuannh.sraft.commons.visitor.Visitor;

@SuppressWarnings("java:S1905")
public interface RpcVisitor extends Visitor<BaseRpc, Void> {
    @Override
    default Void visit(BaseRpc o) {
        if (o instanceof AppendEntries) {
            return visit((AppendEntries)o);
        } else if (o instanceof AppendEntriesResponse) {
            return visit((AppendEntriesResponse)o);
        } else if (o instanceof RequestVote) {
            return visit((RequestVote)o);
        } else if (o instanceof RequestVoteResponse) {
            return visit((RequestVoteResponse)o);
        }
        return null;
    }

    Void visit(AppendEntries message);
    Void visit(AppendEntriesResponse message);
    Void visit(RequestVote message);
    Void visit(RequestVoteResponse message);
}
