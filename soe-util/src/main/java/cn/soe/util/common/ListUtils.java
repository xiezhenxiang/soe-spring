package cn.soe.util.common;

import java.util.List;
import java.util.function.Consumer;

/**
 * @author xiezhenxiang 2023/5/8
 */
public class ListUtils {

    public static <T> void splitExec(List<T> ls, Integer batchSize, Consumer<List<T>> consumer) {
        if (ls == null || ls.isEmpty()) {
            return;
        }
        if (ls.size() <= batchSize) {
            consumer.accept(ls);
            return;
        }
        int index = 0;
        while (index < ls.size()) {
            int endIndex = Math.min(index + batchSize, ls.size());
            consumer.accept(ls.subList(index, endIndex));
            index += batchSize;
        }
    }
}
