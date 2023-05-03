package cn.soe.script.autoconfigure.context;

import cn.soe.boot.autoconfigure.context.SoeEnvironmentPostProcessor;
import cn.soe.boot.core.util.SoeUtils;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * 环境后置处理器
 * 该初始化器是在IOC容器刷新（Bean实例化）前执行，主要定义一些默认参数
 * @author xiezhenxiang 2023/4/25
 */
public class SoeScriptEnvironmentPostProcessor implements EnvironmentPostProcessor, Ordered  {

    public static final int ORDER = SoeEnvironmentPostProcessor.ORDER + 1;;

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        SoeUtils.putFirstEnvProperty(environment, "spring.main.banner-mode", Banner.Mode.OFF.name());
    }

    @Override
    public int getOrder() {
        return ORDER;
    }
}
