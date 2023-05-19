package cn.soe.boot.autoconfigure.context;

import org.springframework.boot.context.logging.LoggingApplicationListener;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.annotation.Order;

/**
 * 应用上下文初始化器
 * 该初始化器是在IOC容器刷新前执行
 * 将{@link SoeBeanFactoryRegistryPostProcessor}添加到上下文中,在常规BDF加载后执行
 * @author xiezhenxiang 2023/5/10
 */
@Order(LoggingApplicationListener.DEFAULT_ORDER + 1)
public class SoeApplicationContextInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public void initialize(ConfigurableApplicationContext context) {
        context.addBeanFactoryPostProcessor(new SoeBeanFactoryRegistryPostProcessor());
    }
}
