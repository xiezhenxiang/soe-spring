package cn.soe.boot.autoconfigure.context;

import cn.soe.boot.core.util.SoeUtils;
import cn.soe.util.common.RegexUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.config.ConfigDataEnvironmentPostProcessor;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import static cn.soe.boot.core.bean.SoeConstant.SOE_FIRST_PROPERTY_SOURCE;

/**
 * 环境后置处理器
 * 该初始化器是在IOC容器刷新（Bean实例化）前执行，主要定义一些默认参数
 * @author xiezhenxiang 2023/4/25
 */
public class SoeEnvironmentPostProcessor implements EnvironmentPostProcessor, Ordered  {

    public static final int ORDER = ConfigDataEnvironmentPostProcessor.ORDER + 1;;
    private static final String SOE_DEFAULT_PROPERTY_FILE = "META-INF/soe-boot-default.properties";
    private static final String SOE_DEFAULT_PROPERTY_SOURCE = "soe-default";

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        // 添加优先级最高的配置源
        environment.getPropertySources().addFirst(new PropertiesPropertySource(SOE_FIRST_PROPERTY_SOURCE, new Properties()));
        // 添加默认配置源
        addDefaultPropertySource(environment, application);
    }

    private void addDefaultPropertySource(ConfigurableEnvironment environment, SpringApplication application) {
        try {
            Resource soeResource = SoeUtils.loadClassFile(SOE_DEFAULT_PROPERTY_FILE, this.getClass());
            Properties soeProperties = PropertiesLoaderUtils.loadProperties(soeResource);
            soeProperties.put("soe.version", "v" + SoeUtils.getImplVersion(this.getClass()));
            soeProperties.put("app.name", environment.getProperty("spring.application.name", "app"));
            soeProperties.put("app.version", "v" + SoeUtils.getImplVersion(application.getMainApplicationClass()));
            soeProperties.put("app.package", ClassUtils.getPackageName(application.getMainApplicationClass()));
            soeProperties.put("logging.file.dir", SoeUtils.isWindows() ? "D:/work/logs": "/work/logs");
            replacePropertySource(environment, soeProperties);
            environment.getPropertySources().addLast(new PropertiesPropertySource(SOE_DEFAULT_PROPERTY_SOURCE, soeProperties));
            // 配置转换：是否开启swagger
            Boolean swaggerEnable = environment.getProperty("swagger.enable", Boolean.class, true);
            if (!swaggerEnable) {
                soeProperties.put("knife4j.production", true);
            }
        } catch (IOException e) {
            throw new IllegalArgumentException("cant not load soe-default property source!");
        }
    }

    /**
     * 替换占位符变量
     * @author xiezhenxiang 2023/4/27
     **/
    private void replacePropertySource(ConfigurableEnvironment environment, Properties properties) {
        for (Map.Entry<Object, Object> entry : properties.entrySet()) {
            String value = entry.getValue().toString();
            List<String> expressStrLs = RegexUtils.extractFromHeadTail(value, "${", "}", true);
            if (!expressStrLs.isEmpty()) {
                for (String expressStr : expressStrLs) {
                    int index = expressStr.lastIndexOf(":");
                    expressStr = index > 0 ? expressStr.substring(0, index) + "}" : expressStr;
                    String expressValue = environment.resolvePlaceholders(expressStr);
                    if (StringUtils.hasText(expressStr) && !expressValue.equals(expressStr)) {
                        value = value.replace(expressStr, expressValue);
                    }
                }
                entry.setValue(value);
            }
        }
    }

    @Override
    public int getOrder() {
        return ORDER;
    }
}
