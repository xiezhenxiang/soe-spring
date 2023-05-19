package cn.soe.boot.autoconfigure.web.swagger;

import cn.soe.boot.autoconfigure.web.SoeWebConfiguration;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.apache.commons.lang3.RandomUtils;
import org.springdoc.core.GroupedOpenApi;
import org.springdoc.core.customizers.GlobalOpenApiCustomizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import java.util.HashMap;
import java.util.Map;

/**
 * @author xiezhenxiang 2021/9/13
 */
@SoeWebConfiguration
@EnableConfigurationProperties(SoeSwaggerProperties.class)
public class SoeSwaggerConfiguration {

    @Value("${app.name}")
    private String appName;
    @Value("${app.version}")
    private String appVersion;
    @Value("${app.package}")
    private String appPackage;

    @Bean
    public OpenAPI customOpenApi() {
        return new OpenAPI().info(new Info()
                    .title("「"+ appName +"」服务API")
                    .version(appVersion)
                    .description( "the documents of "+ appName +"'s restful apis")
                    .termsOfService("http://doc.xiaominfo.com")
                    .license(new License().name("Apache 2.0")
                    .url("http://doc.xiaominfo.com")));
    }

    @Bean
    public GroupedOpenApi customGroupedOpenApi(){
        String[] paths = { "/**" };
        String[] packagedToMatch = {appPackage};
        return GroupedOpenApi.builder().group(appName)
                .pathsToMatch(paths)
                .packagesToScan(packagedToMatch).build();
    }

    /**
     * 根据@Tag 上的排序，写入x-order
     * @return the global open api customizer
     */
    @Bean
    public GlobalOpenApiCustomizer orderGlobalOpenApiCustomizer() {
        return openApi -> {
            if (openApi.getTags()!=null){
                openApi.getTags().forEach(tag -> {
                    Map<String,Object> map=new HashMap<>();
                    map.put("x-order", RandomUtils.nextInt(1,100));
                    tag.setExtensions(map);
                });
            }
            if(openApi.getPaths()!=null){
                openApi.getPaths().addExtension("x-abb", RandomUtils.nextInt(1,100));
            }
        };
    }
}
