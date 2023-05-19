package cn.soe.boot.autoconfigure.mongo;

import cn.soe.util.database.mongo.MongoUtils;
import com.mongodb.client.MongoClient;

/**
 * @author xiezhenxiang 2023/5/12
 */
public class MongoSdk extends MongoUtils {

    public MongoSdk(MongoClient mongoClient) {
        super(mongoClient);
    }
}
