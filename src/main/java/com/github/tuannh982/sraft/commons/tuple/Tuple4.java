package com.github.tuannh982.sraft.commons.tuple;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
public class Tuple4<V0, V1, V2, V3> implements Tuple {
    private final V0 v0;
    private final V1 v1;
    private final V2 v2;
    private final V3 v3;

    @Override
    public Object[] toArray() {
        return new Object[]{v0, v1, v2, v3};
    }

    @Override
    public Object get(int index) {
        switch (index) {
            case 0:
                return v0;
            case 1:
                return v1;
            case 2:
                return v2;
            case 3:
                return v3;
            default:
                throw new IndexOutOfBoundsException();
        }
    }
}
