package cn.soe.boot.core.util;

import cn.soe.util.common.ObjectUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

import java.net.MalformedURLException;
import java.util.Objects;

import static cn.soe.boot.core.bean.SoeConstant.SOE_FIRST_PROPERTY_SOURCE;

/**
 * @author xiezhenxiang 2023/4/25
 */
public class SoeUtils {

    private static volatile ApplicationContext applicationContext;

    public static Object getBean(String name) {
        return getApplicationContext().getBean(name);
    }

    public static <T> T getBean(String name, Class<T> clazz) {
        return getApplicationContext().getBean(name,clazz);
    }

    public static <T> T getBean(Class<T> clazz) {
        return getApplicationContext().getBean(clazz);
    }

    public static void setApplicationContext(ApplicationContext ac) {
        if(applicationContext == null || applicationContext.getParent() != null){//if context is child
            applicationContext = ac;
        }
    }

    public static ApplicationContext getApplicationContext() {
        Assert.notNull(applicationContext, "applicationContext not inject yet");
        return applicationContext;
    }

    public static boolean isWindows() {
        return System.getProperty("os.name").toLowerCase().startsWith("windows");
    }

    public static Resource loadClassFile(String filePath, Class<?> clazz) throws MalformedURLException {
        filePath = filePath.startsWith("/") ? filePath.substring(1) : filePath;
        String path = Objects.requireNonNull(clazz.getResource("")).getPath();
        if (path.contains("file:")) {
            path = path.replaceFirst("file:", "jar:file:");
        } else {
            path = "file:" + path;
        }
        path = path.endsWith("/") ? path.substring(0, path.length() - 1) : path;
        String packageName = ClassUtils.getPackageName(clazz).replace(".", "/");
        path = path.replace(packageName, filePath);
        return new UrlResource(path);
    }

    public static String getImplVersion(Class<?> clazz) {
        String version = clazz.getPackage().getImplementationVersion();
        return version == null ? "0.0.0" : version;
    }

    public static <T> T getEnvProperty(String key, Class<T> clazz) {
        return getBean(ConfigurableEnvironment.class).getProperty(key, clazz);
    }

    /**
     * 添加优先级最高的配置
     **/
    public static void putFirstEnvProperty(ConfigurableEnvironment environment, String key, Object value) {
        environment = ObjectUtils.ofEmpty(environment, () -> getBean(ConfigurableEnvironment.class));
        PropertiesPropertySource propertySource = (PropertiesPropertySource) environment.getPropertySources().get(SOE_FIRST_PROPERTY_SOURCE);
        Assert.notNull(propertySource, "not set soe-first property source!");
        propertySource.getSource().put(key, value);
    }

    public static void putFirstEnvProperty(String key, Object value) {
        putFirstEnvProperty(null, key, value);
    }
}
