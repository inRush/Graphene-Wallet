package com.gxb.gxswallet.utils;

import java.util.HashSet;
import java.util.List;

/**
 * @author inrush
 * @date 2018/3/12.
 */

public class ListUtil {
    public static void uniqList(List list) {
        HashSet h = new HashSet(list);
        list.clear();
        list.addAll(h);
    }

}
