package cn.soe.util.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

/**
 * @author xiezhenxiang 2023/5/8
 */
@Slf4j
public class JsonUtils {
    private static ObjectMapper objectMapper = new ObjectMapper();
    static {
        configObjectMapper();
    }

    public static ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    public static void setObjectMapper(ObjectMapper objectMapper){
        JsonUtils.objectMapper = objectMapper;
        configObjectMapper();
    }

    private static void configObjectMapper() {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        // not serial null value
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        objectMapper.setTimeZone(TimeZone.getTimeZone("GMT+8"));
    }

    public static TypeFactory getTypeFactory(){
        return getObjectMapper().getTypeFactory();
    }

    public static <T> T parseObject(Object obj, Class<?> parametrized, Class<?>... parameterClasses) {
        try {
            JavaType javaType = getTypeFactory().constructParametricType(parametrized, parameterClasses);
            return objectMapper.readValue(toJsonString(obj), javaType);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    public static <T> T parseObject(Object content, JavaType javaType){
        try {
            return getObjectMapper().readValue(toJsonString(content), javaType);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    public static <T> T parseObject(Object obj, TypeReference<T> typeReference) {
        try {
            return objectMapper.readValue(toJsonString(obj), typeReference);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    public static String toJsonString(Object obj) {
        try {
            return obj instanceof String ? obj.toString() : objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    public static <T> T parseObject(Object obj, Class<T> clazz) {
        try {
            return objectMapper.readValue(toJsonString(obj), clazz);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    public static <T> List<T> parseList(Object content, Class<T> clazz) {
        return parseObject(toJsonString(content), getTypeFactory().constructCollectionType(List.class, clazz));
    }

    public static Map<String, Object> parseMap(Object content) {
        return parseMap(content, String.class, Object.class);
    }

    public static <K, V> Map<K, V> parseMap(Object content, Class<K> keyClass, Class<V> valueClass) {
        return parseObject(toJsonString(content), getTypeFactory().constructMapType(Map.class, keyClass, valueClass));
    }

    public static Map<String, Object> parseMapList(Object content) {
        return parseObject(toJsonString(content), Map.class, String.class, Object.class);
    }

    public static <K, V> List<Map<K, V>> parseMapList(Object content, Class<K> keyClass, Class<V> valueClass) {
        return parseObject(toJsonString(content), getTypeFactory().constructCollectionType(List.class, getTypeFactory().constructMapType(Map.class, keyClass, valueClass)));
    }
}
