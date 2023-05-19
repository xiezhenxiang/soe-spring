package cn.soe.boot.autoconfigure.web;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AliasFor;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.Servlet;
import java.lang.annotation.*;

/**
 * @author xiezhenxiang 2023/5/6
 **/
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Configuration
@ConditionalOnClass
@ConditionalOnWebApplication
public @interface SoeWebConfiguration {

    @AliasFor(annotation = Configuration.class)
    boolean proxyBeanMethods() default true;

    @AliasFor(annotation = ConditionalOnClass.class)
    Class<?>[] value() default {Servlet.class, DispatcherServlet.class};

    @AliasFor(annotation = ConditionalOnWebApplication.class)
    ConditionalOnWebApplication.Type type() default ConditionalOnWebApplication.Type.SERVLET;
}
