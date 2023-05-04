package cn.soe.boot.autoconfigure.web.filter.cors;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * @author xiezhenxiang 2021/9/13
 */
@Configuration
@EnableConfigurationProperties(CorsProperties.class)
@ConditionalOnProperty(prefix = "soe.cors", name = "enable", havingValue = "true")
public class CorsFilterConfiguration {

    private final CorsProperties corsProperties;

    public CorsFilterConfiguration(CorsProperties corsProperties) {
        this.corsProperties = corsProperties;
    }

    @Bean
    public FilterRegistrationBean<CorsFilter> corsFilterRegistration(CorsConfigurationSource corsConfigurationSource) {
        FilterRegistrationBean<CorsFilter> filterRegistrationBean = new FilterRegistrationBean<>(new CorsFilter(corsConfigurationSource));
        filterRegistrationBean.setOrder(corsProperties.getOrder());
        filterRegistrationBean.setName(corsProperties.getName());
        return filterRegistrationBean;
    }

    @Bean
    @ConditionalOnMissingBean(name = "corsConfigurationSource")
    public CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowCredentials(corsProperties.getAllowCredentials());
        corsConfiguration.addAllowedOrigin(corsProperties.getAllowedOrigin());
        corsConfiguration.addAllowedHeader(corsProperties.getAllowedHeader());
        corsConfiguration.addAllowedMethod(corsProperties.getAllowedMethod());
        corsConfiguration.setMaxAge(corsProperties.getMaxAge());
        corsConfiguration.setExposedHeaders(corsProperties.getExposedHeaders());
        source.registerCorsConfiguration(corsProperties.getPattern(), corsConfiguration);
        return source;
    }
}