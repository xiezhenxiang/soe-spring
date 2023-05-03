package cn.soe.script.autoconfigure.context;

import cn.soe.script.autoconfigure.SoeCommandLineRunner;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.stream.Collectors;

/**
 * @author xiezhenxiang 2023/5/3
 */
public class SoeScriptBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar, ResourceLoaderAware, EnvironmentAware {

    private ResourceLoader resourceLoader;

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry, BeanNameGenerator importBeanNameGenerator) {
        SoeScriptBeanDefinitionScanner scanner = new SoeScriptBeanDefinitionScanner(registry);
        // this check is needed in Spring 3.1
        if (resourceLoader != null) {
            scanner.setResourceLoader(resourceLoader);
        }
        List<String> basePackages = getAllSubClass(SoeCommandLineRunner.class).stream().map(ClassUtils::getPackageName).collect(Collectors.toList());
        if (!basePackages.isEmpty()) {
            scanner.doScan(StringUtils.toStringArray(basePackages));
        }
    }

    /**
     * 获取指定类的所有子类
     */
    private static List<Class<?>> getAllSubClass(Class<?> clazz)  {
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

    @Override
    public void setEnvironment(Environment environment) {

    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }
}
