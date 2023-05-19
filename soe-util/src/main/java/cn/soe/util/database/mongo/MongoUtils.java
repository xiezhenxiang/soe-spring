package cn.soe.util.database.mongo;

import cn.soe.util.database.DbConnectTest;
import com.google.common.collect.Lists;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.*;
import com.mongodb.client.model.InsertManyOptions;
import com.mongodb.client.model.ReplaceOptions;
import com.mongodb.client.model.UpdateManyModel;
import com.mongodb.client.model.UpdateOptions;
import lombok.extern.slf4j.Slf4j;
import org.bson.BsonDocument;
import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.conversions.Bson;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

/**
 * @author xiezhenxiang 2019/4/17
 */
@Slf4j
public class MongoUtils implements DbConnectTest {

    private List<ServerAddress> urlList;
    private String userName;
    private String password;
    private volatile MongoClient client;
    private final Map<Integer, MongoClient> pool = new HashMap<>(10);

    private static final Integer BATCH_SIZE = 10000;
    public static final CodecRegistry CODE_REGISTRY = CodecRegistries.fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
            CodecRegistries.fromProviders(PojoCodecProvider.builder().automatic(true).build()));

    private MongoUtils(String ip, Integer port, String userName, String password) {
        String[] ips = ip.split(",");
        urlList = new ArrayList<>();
        for (String one : ips) {
            urlList.add(new ServerAddress(one, port));
        }
        this.userName = userName;
        this.password = password;
        initClient();
    }

    public MongoUtils(MongoClient mongoClient) {
        this.client = mongoClient;
    }

    public static MongoUtils getInstance(String ip, Integer port, String userName, String password) {
        return new MongoUtils(ip, port, userName, password);
    }

    public static MongoUtils getInstance(String hosts, String userName, String password) {
        return new MongoUtils(hosts, userName, password);
    }

    private MongoUtils(String hosts, String userName, String password) {
        urlList = new ArrayList<>();
        String[] hostArr = hosts.split(",");
        for (String one : hostArr) {
            String[] ipPort = one.split(":");
            urlList.add(new ServerAddress(ipPort[0], Integer.parseInt(ipPort[1])));
        }
        this.userName = userName;
        this.password = password;
        initClient();
    }

    public MongoCursor<Document> find(String db, String col, Bson query, Bson sort) {
        return find(db, col, query, sort, null, null);
    }

    public  MongoCursor<Document> find(String db, String col, Bson query) {
        return find(db, col, query, null, null, null);
    }

    public MongoCursor<Document> find(String db, String col, Bson query, Bson sort, Integer pageNo, Integer pageSize) {
        initClient();
        MongoCursor<Document> mongoCursor = null;
        query = query == null ? new Document() : query;
        sort = sort == null ? new Document() : sort;

        FindIterable<Document> findIterable = client.getDatabase(db).getCollection(col).find(query).sort(sort);
        if(pageNo != null) {
            pageNo = (pageNo - 1) * pageSize;
            findIterable.skip(pageNo);
        }
        if (pageSize != null) {
            findIterable.limit(pageSize);
        }
        mongoCursor = findIterable.batchSize(BATCH_SIZE).maxAwaitTime(10L, TimeUnit.MINUTES).iterator();
        return mongoCursor;
    }

    public MongoCursor<Document> aggregate(String db, String col, List<Bson> aggLs) {
        initClient();
        return client.getDatabase(db).getCollection(col).aggregate(aggLs).allowDiskUse(true).batchSize(BATCH_SIZE)
                .maxTime(15L, TimeUnit.MINUTES).cursor();
    }

    public void insertMany(String database, String collection, List<Document> documentList) {
        initClient();
        if (documentList == null || documentList.isEmpty()) {
            return;
        }
        InsertManyOptions insertManyOptions = new InsertManyOptions();
        insertManyOptions.ordered(false);
        try {
            client.getDatabase(database).getCollection(collection).insertMany(documentList, insertManyOptions);
        } catch (Exception e) {
            // 唯一约束的skip, 不然插入会丢数据
            if (!e.getMessage().contains("duplicate")) {
                throw new RuntimeException(e);
            }
        }
    }

    public void insertOne(String database, String collection, Document doc) {
        initClient();
        client.getDatabase(database).getCollection(collection).insertOne(doc);
    }

    public void updateOne(String database, String collection, Bson query, Document doc) {
        initClient();
        client.getDatabase(database).getCollection(collection).updateOne(query, new Document("$set", doc));
    }

    public void upsertOne(String database, String collection, Bson query, Document doc) {
        initClient();
        client.getDatabase(database).getCollection(collection).replaceOne(query, doc, new ReplaceOptions().upsert(true));
    }

    public void push(String database, String collection, Bson query, String field, Object... value) {
        initClient();
        Document pushDoc = new Document("$push", new Document(field, new Document("$each", Lists.newArrayList(value))));
        client.getDatabase(database).getCollection(collection).updateMany(query, pushDoc);
    }

    public void upsertMany(String database, String collection, Collection<Document> ls, boolean upsert, String... fieldArr) {
        initClient();
        if (ls == null || ls.isEmpty()) {
            return;
        }

        List<UpdateManyModel<Document>> requests = ls.stream().map(s -> new UpdateManyModel<Document>(
                new Bson() {
                    @Override
                    public <TDocument> BsonDocument toBsonDocument(Class<TDocument> aClass, CodecRegistry codecRegistry) {
                        Document doc = new Document();
                        for (String field : fieldArr) {
                            doc.append(field, s.get(field));
                        }
                        return doc.toBsonDocument(aClass, codecRegistry);
                    }
                },
                new Document("$set",s),
                new UpdateOptions().upsert(upsert)
        )).collect(Collectors.toList());
        client.getDatabase(database).getCollection(collection).bulkWrite(requests);
    }

    public void updateMany(String database, String collection, Bson query, Document doc) {
        initClient();
        client.getDatabase(database).getCollection(collection).updateMany(query, new Document("$set", doc));
    }

    public Long count(String db, String col, Bson query){
        initClient();
        return client.getDatabase(db).getCollection(col).countDocuments(query);
    }

    public List<Document> getIndex(String db, String col) {
        initClient();
        List<Document> indexLs = new ArrayList<>();
        MongoCursor<Document> cursor = client.getDatabase(db).getCollection(col).listIndexes().iterator();
        cursor.forEachRemaining(s -> indexLs.add((Document) s.get("key")));
        return indexLs;
    }

    public void delete(String db, String col, Bson query){
        initClient();
        client.getDatabase(db).getCollection(col).deleteMany(query);
    }

    public void createIndex(String db, String col, Document... indexArr) {
        initClient();
        for (Document index : indexArr) {
            client.getDatabase(db).getCollection(col).createIndex(index);
        }
    }

    public void dropIndex(String db, String col, Document... indexArr) {
        initClient();
        for (Document index : indexArr) {
            client.getDatabase(db).getCollection(col).dropIndex(index);
        }
    }


    public synchronized void copyDataBase(String fromDbName, String toDbName) {
        initClient();
        MongoIterable<String> colNames = client.getDatabase(fromDbName).listCollectionNames();

        for (String colName : colNames) {
            copyCollection(fromDbName, colName, toDbName, colName);
        }
    }

    public void copyCollection(String fromDbName, String fromColName, String toDbName, String toColName) {
        initClient();
        copyCollection(this, fromDbName, fromColName, this, toDbName, toColName);
    }

    public void copyCollection(String fromDbName, String fromColName, MongoUtils toMongoUtil, String toDbName, String toColName) {
        initClient();
        copyCollection(this, fromDbName, fromColName, toMongoUtil, toDbName, toColName);
    }

    private void copyCollection(MongoUtils fromMongo, String fromDbName, String fromColName, MongoUtils toMongo, String toDbName, String toColName) {
        initClient();
        List<Document> indexLs = fromMongo.getIndex(fromDbName, fromColName);
        toMongo.createIndex(toDbName, toColName,  indexLs.toArray(new Document[0]));
        MongoCursor<Document> cursor = fromMongo.find(fromDbName, fromColName, null);
        List<Document> docLs = new ArrayList<>();
        cursor.forEachRemaining(doc -> {
            docLs.add(doc);
            if (docLs.size() >= BATCH_SIZE) {
                toMongo.insertMany(toDbName, toColName, docLs);
                docLs.clear();
            }
        });
        toMongo.insertMany(toDbName, toColName, docLs);
    }

    private void initClient() {
        if (client == null) {
            synchronized (MongoUtils.class){
                if (client == null) {
                    Integer key = urlList.stream().map(s -> (s.getHost() + s.getPort()).hashCode()).reduce(Integer::sum).orElse(0);
                    if (pool.containsKey(key)) {
                        client = pool.get(key);
                        if (client == null) {
                            initClient();
                        }
                    } else {
                        MongoCredential mongoCredential = MongoCredential.createScramSha1Credential(userName, "admin", password.toCharArray());
                        MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
                            .applyToConnectionPoolSettings(connectionPool -> {
                                connectionPool.maxSize(100);
                                connectionPool.maxWaitTime(1000 * 60 * 10, MILLISECONDS);
                            })
                            .applyToClusterSettings(cluster -> {
                                cluster.hosts(urlList);
                                cluster.serverSelectionTimeout(5000, MILLISECONDS);
                            })
                            .applyToSocketSettings(socket -> {
                                socket.connectTimeout(3000, MILLISECONDS);
                                socket.readTimeout(0, MILLISECONDS);
                            })
                            .credential(mongoCredential)
                            .build();
                        client = MongoClients.create(mongoClientSettings);
                        pool.put(key, client);
                    }
                }
            }
        }
    }

    @Override
    public void testConnect() {
        try {
            client.getDatabase("test").getCollection("test").countDocuments();
        } catch (Exception e) {
            log.error("mongo connect exception", e);
        }
    }

    public MongoClient getClient() {
        initClient();
        return client;
    }

    public static void main(String[] args) {
        MongoUtils mongoUtils = getInstance("localhost:27017", "admin", "admin");
        mongoUtils.find("ss", "ss", null);
    }
}
