package cn.soe.boot.autoconfigure.web;

import cn.soe.boot.autoconfigure.web.exception.BizExceptionHandler;
import cn.soe.boot.autoconfigure.web.exception.SoeErrorControllerHandler;
import cn.soe.boot.autoconfigure.web.swagger.SoeSwaggerConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.Servlet;

/**
 * @author xiezhenxiang 2023/4/27
 **/
@Configuration
@AutoConfigureBefore({ErrorMvcAutoConfiguration.class})
@ConditionalOnClass({Servlet.class, DispatcherServlet.class})
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@Import({BizExceptionHandler.class, SoeSwaggerConfiguration.class})
public class SoeSpringMvcAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(ErrorController.class)
    public SoeErrorControllerHandler soeErrorControllerHandler() {
        return new SoeErrorControllerHandler();
    }
}
