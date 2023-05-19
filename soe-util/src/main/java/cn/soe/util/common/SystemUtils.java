package cn.soe.util.common;

/**
 * @author xiezhenxiang 2022/3/8
 */
public class SystemUtils {

    public static boolean isWindows() {
        return System.getProperty("os.name").toLowerCase().startsWith("windows");
    }
}
