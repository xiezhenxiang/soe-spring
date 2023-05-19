package cn.soe.util.common;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * @author xiezhenxiang 2023/5/3
 */
public class ClassUtils {

    /**
     * 获取指定类的所有子类
     */
    public static List<Class<?>> getAllSubClass(Class<?> clazz)  {
        List<Class<?>> ls = new ArrayList<>();
        try {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            Class<?> classOfClassLoader = classLoader.getClass();
            while (classOfClassLoader != ClassLoader.class) {
                classOfClassLoader = classOfClassLoader.getSuperclass();
            }
            Field field = classOfClassLoader.getDeclaredField("classes");
            field.setAccessible(true);
            Vector<?> v = (Vector<?>) field.get(classLoader);
            for (Object o : v) {
                Class<?> c = (Class<?>) o;
                // 去掉代理类和自身
                if (!c.getName().contains("$") & clazz.isAssignableFrom(c) && !clazz.equals(c)) {
                    ls.add(c);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ls;
    }

    /**
     * 获取项目所在的绝对路径
     **/
    public static String getProjectDir(Class<?> cls) {

        String path = cls.getProtectionDomain().getCodeSource().getLocation().getPath();
        try{
            path = java.net.URLDecoder.decode(path, "UTF-8").replace("\\", "/");
        }catch (Exception e){
            e.printStackTrace();
        }
        int lastIndex = path.lastIndexOf("/", path.length() -2);
        path = path.substring(0, lastIndex + 1);
        // class文件在jar包中
        if (path.contains("file")) {
            path = path.replace("file:", "");
            int breakIndex = path.indexOf("!/");
            if (breakIndex > 0) {
                path = path.substring(0, path.lastIndexOf("/", breakIndex) + 1);
            }
        }
        return path;
    }
}
