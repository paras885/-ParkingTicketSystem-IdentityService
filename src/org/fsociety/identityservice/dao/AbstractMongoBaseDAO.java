package org.fsociety.identityservice.dao;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.fsociety.identityservice.config.MongoDBConfiguration;

import javax.annotation.Resource;

public abstract class AbstractMongoBaseDAO {

    @Resource(name = MongoDBConfiguration.MONGO_DATABASE_BEAN_NAME)
    protected MongoDatabase mongoDatabase;

    protected MongoCollection getMongoCollection(final String collectionName, Class collectionClassName) {
        return mongoDatabase.getCollection(collectionName, collectionClassName);
    }
}
