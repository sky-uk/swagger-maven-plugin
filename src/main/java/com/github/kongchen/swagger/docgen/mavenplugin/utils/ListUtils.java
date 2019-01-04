package com.github.kongchen.swagger.docgen.mavenplugin.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ListUtils {
    public static <T> List<T> concat(List<? extends T> fst, List<? extends T> snd) {
        if (fst == null) {
            fst = Collections.emptyList();
        }
        if (snd == null) {
            snd = Collections.emptyList();
        }
        ArrayList<T> list = new ArrayList<>(fst);
        list.addAll(snd);
        return list;
    }
}
