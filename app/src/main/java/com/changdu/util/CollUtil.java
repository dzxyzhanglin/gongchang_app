package com.changdu.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class CollUtil {

    /**
     * 判断集合是否为空
     * @param collection
     * @return
     */
    public static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    /**
     * 删除数组中的的某个元素
     * @param collection
     * @param position
     * @return
     */
    public static List<Map<String, Object>> remove(List<Map<String, Object>> collection, int position) {
        List<Map<String, Object>> lists = new ArrayList<>();

        int idx = 0;
        for (Map<String, Object> col : collection) {
            if (idx != position) {
                lists.add(col);
            }
            idx++;
        }
        return lists;
    }
}
