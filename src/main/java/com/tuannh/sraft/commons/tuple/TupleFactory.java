package com.tuannh.sraft.commons.tuple;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TupleFactory {
    public static <V0, V1> Tuple2<V0, V1> of(V0 v0, V1 v1) {
        return new Tuple2<>(v0, v1);
    }

    public static <V0, V1, V2> Tuple3<V0, V1, V2> of(V0 v0, V1 v1, V2 v2) {
        return new Tuple3<>(v0, v1, v2);
    }

    public static <V0, V1, V2, V3> Tuple4<V0, V1, V2, V3> of(V0 v0, V1 v1, V2 v2, V3 v3) {
        return new Tuple4<>(v0, v1, v2, v3);
    }
}
