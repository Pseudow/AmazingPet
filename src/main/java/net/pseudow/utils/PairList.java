package net.pseudow.utils;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

public class PairList<T0, T1> extends ArrayList<Map.Entry<T0, T1>> {

    private static final long serialVersionUID = -700739748090276478L;

    public PairList() { super(); }

    public PairList(T0 a, T1 b) {
        super();
        add(new AbstractMap.SimpleEntry<T0, T1>(a, b));
    }

    @SafeVarargs
    public PairList(Map.Entry<T0, T1>... entries) {
        super();
        Arrays.asList(entries).forEach(super::add);
    }
}
