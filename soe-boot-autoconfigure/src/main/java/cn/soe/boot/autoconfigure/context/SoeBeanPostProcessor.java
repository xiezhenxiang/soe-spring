package cn.soe.boot.autoconfigure.context;

import cn.soe.util.common.JsonUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

/**
 * bean后置处理器
 * @author xiezhenxiang 2023/4/25
 */
public class SoeBeanPostProcessor implements BeanPostProcessor{

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if(bean instanceof ObjectMapper){
            JsonUtils.setObjectMapper((ObjectMapper) bean);
        }
        return bean;
    }
}