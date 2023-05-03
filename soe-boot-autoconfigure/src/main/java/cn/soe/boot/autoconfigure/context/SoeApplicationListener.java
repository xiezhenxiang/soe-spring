package cn.soe.boot.autoconfigure.context;

import cn.soe.boot.core.util.SoeUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.event.*;
import org.springframework.boot.context.logging.LoggingApplicationListener;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.GenericApplicationListener;
import org.springframework.core.ResolvableType;

/**
 * Application事件监听器
 * @author xiezhenxiang 2023/4/25
 */
public class SoeApplicationListener implements GenericApplicationListener {

    private static final Class<?>[] EVENT_TYPES = { SpringApplicationEvent.class};
    private static final Class<?>[] SOURCE_TYPES = { SpringApplication.class };

    @Override
    public void onApplicationEvent(ApplicationEvent applicationEvent) {
        if(applicationEvent instanceof ApplicationStartingEvent){
            onApplicationStartingEvent((ApplicationStartingEvent) applicationEvent);
        }else if(applicationEvent instanceof ApplicationEnvironmentPreparedEvent){
            onApplicationEnvironmentPreparedEvent((ApplicationEnvironmentPreparedEvent) applicationEvent);
        }else if(applicationEvent instanceof ApplicationContextInitializedEvent){
            onApplicationContextInitializedEvent((ApplicationContextInitializedEvent) applicationEvent);
        }else if(applicationEvent instanceof ApplicationPreparedEvent){
            onApplicationPreparedEvent((ApplicationPreparedEvent)applicationEvent);
        }else if(applicationEvent instanceof ApplicationReadyEvent){
            onApplicationReadyEvent((ApplicationReadyEvent) applicationEvent);
        }
    }

    private void onApplicationStartingEvent(ApplicationStartingEvent event) {

    }

    private void onApplicationEnvironmentPreparedEvent(ApplicationEnvironmentPreparedEvent event) {

    }

    private void onApplicationContextInitializedEvent(ApplicationContextInitializedEvent event){

    }

    private void onApplicationPreparedEvent(ApplicationPreparedEvent event){
        SoeUtils.setApplicationContext(event.getApplicationContext());
    }

    private void onApplicationReadyEvent(ApplicationReadyEvent event) {

    }

    @Override
    public boolean supportsEventType(ResolvableType resolvableType) {
        return isAssignableFrom(resolvableType.getRawClass(), EVENT_TYPES);
    }

    @Override
    public boolean supportsSourceType(Class<?> sourceType) {
        return isAssignableFrom(sourceType, SOURCE_TYPES);
    }

    private boolean isAssignableFrom(Class<?> type, Class<?>... supportedTypes) {
        if (type != null) {
            for (Class<?> supportedType : supportedTypes) {
                if (supportedType.isAssignableFrom(type)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public int getOrder() {
        return LoggingApplicationListener.DEFAULT_ORDER + 1;
    }

}