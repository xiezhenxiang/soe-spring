package cn.soe.boot.autoconfigure.web;

import cn.soe.boot.autoconfigure.web.actuator.SoeControllerEndPoint;
import cn.soe.boot.autoconfigure.web.exception.BizExceptionHandler;
import cn.soe.boot.autoconfigure.web.exception.SoeErrorControllerHandler;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.context.annotation.Bean;

/**
 * @author xiezhenxiang 2023/4/27
 **/
@SoeWebConfiguration
@AutoConfigureBefore({ErrorMvcAutoConfiguration.class})
public class SoeWebAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(ErrorController.class)
    public SoeErrorControllerHandler errorControllerHandler() {
        return new SoeErrorControllerHandler();
    }

    @Bean
    public BizExceptionHandler bizExceptionHandler() {
        return new BizExceptionHandler();
    }

    @Bean
    public SoeControllerEndPoint controllerEndPoint() {
        return new SoeControllerEndPoint();
    }
}
