package cn.soe.util.common;

import java.util.Collection;

/**
 * @author xiezhenxiang 2023/5/8
 */
public class CollectUtils {

    public static <T> boolean isEmpty(Collection<T> ls) {
        return ls == null || ls.isEmpty();
    }

    public static <T> boolean notEmpty(Collection<T> ls) {
        return !isEmpty(ls);
    }
}
