package cn.soe.boot.autoconfigure.mongo;

import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import lombok.extern.slf4j.Slf4j;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.mongo.MongoClientSettingsBuilderCustomizer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import java.util.concurrent.TimeUnit;

/**
 * @author xiezhenxiang 2023/5/9
 */
@AutoConfiguration
@ConditionalOnClass(MongoClient.class)
@EnableConfigurationProperties(MongoExtendProperties.class)
@Slf4j
public class MongoExtendAutoConfiguration {

    @Bean
    public MongoClientSettingsBuilderCustomizer customizer(MongoExtendProperties properties){
        return builder -> {
            builder.applyToConnectionPoolSettings(connectionPool -> {
                connectionPool.maxWaitTime(properties.getConnectTimeOut(), TimeUnit.MILLISECONDS);
            });
            builder.applyToClusterSettings(cluster -> {
                cluster.serverSelectionTimeout(properties.getConnectTimeOut(), TimeUnit.MILLISECONDS);
            });
            builder.applyToSocketSettings(socket -> {
                socket.connectTimeout(properties.getConnectTimeOut(), TimeUnit.MILLISECONDS);
                socket.readTimeout(properties.getReadTimeOut(), TimeUnit.MILLISECONDS);
            });
            builder.codecRegistry(CodecRegistries.fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
                    CodecRegistries.fromProviders(PojoCodecProvider.builder().automatic(true).build())));
        };
    }

    @Bean
    public MongoSdk mongoSdk(MongoClient mongoClient) {
        MongoSdk mongoSdk = new MongoSdk(mongoClient);
        mongoSdk.testConnect();
        return mongoSdk;
    }
}
