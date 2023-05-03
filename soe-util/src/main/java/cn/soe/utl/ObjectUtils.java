package cn.soe.utl;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * @author xiezhenxiang 2023/4/28
 */
public class ObjectUtils {

    public static boolean isEmpty( Object obj) {
        if (obj == null) {
            return true;
        } else if (obj instanceof Optional) {
            return !((Optional<?>)obj).isPresent();
        } else if (obj instanceof CharSequence) {
            return ((CharSequence)obj).toString().trim().length() == 0;
        } else if (obj.getClass().isArray()) {
            return Array.getLength(obj) == 0;
        } else if (obj instanceof Collection) {
            return ((Collection<?>)obj).isEmpty();
        } else {
            return obj instanceof Map && ((Map<?, ?>) obj).isEmpty();
        }
    }

    public static boolean isNotEmpty(Object obj) {
        return !isEmpty(obj);
    }

    /**
     * 如果obj为空，则返回defaultObj
     * @author xiezhenxiang 2023/4/28
     **/
    public static <T> T ofEmpty(T obj, T defaultObj) {
        return isEmpty(obj) ? defaultObj : obj;
    }

    /**
     * 如果obj为空，则自定义方法返回值
     * @author xiezhenxiang 2023/4/28
     **/
    public static <T> T ofEmpty(T obj, Supplier<T> defaultSupplier) {
        return isEmpty(obj) ? defaultSupplier.get() : obj;
    }
}
