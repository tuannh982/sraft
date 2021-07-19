package com.github.tuannh982.sraft.commons.tuple;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
public class Tuple3<V0, V1, V2> implements Tuple {
    private final V0 v0;
    private final V1 v1;
    private final V2 v2;

    @Override
    public Object[] toArray() {
        return new Object[]{v0, v1, v2};
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
            default:
                throw new IndexOutOfBoundsException();
        }
    }
}
