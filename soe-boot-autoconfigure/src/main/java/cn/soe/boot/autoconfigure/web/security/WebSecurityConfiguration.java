package cn.soe.boot.autoconfigure.web.security;

import cn.soe.boot.autoconfigure.web.SoeWebConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @see <a href="https://blog.csdn.net/qq_18841277/article/details/128247535">...</a>
 * @author xiezhenxiang 2023/4/27
 **/
@SoeWebConfiguration
@ConditionalOnClass(WebSecurity.class)
@EnableConfigurationProperties(WebSecurityProperties.class)
public class WebSecurityConfiguration {

    private final String[] defaultExcludePaths = {"/v2/api-docs", "/swagger-resources/**", ",/doc.html", "/webjars/**", "/error", "/favicon.ico"};

    private final WebSecurityProperties webSecurityProperties;

    public WebSecurityConfiguration(WebSecurityProperties webSecurityProperties){
        this.webSecurityProperties = webSecurityProperties;
    }


    @Bean
    public WebSecurityCustomizer webSecurityCustomizer(){
        return web -> {
            List<String> paths = new ArrayList<>(Arrays.asList(defaultExcludePaths));
            if(webSecurityProperties.getExcludePaths() != null){
                paths.addAll(Arrays.asList(webSecurityProperties.getExcludePaths()));
            }
            if(!paths.isEmpty()){
                web.ignoring().requestMatchers(new OrRequestMatcher(paths.stream().map(AntPathRequestMatcher::new).collect(Collectors.toList())));
            }
        };
    }
}
