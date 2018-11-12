package org.fsociety.identityservice.dao;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.pts.common.entities.Building;
import com.pts.common.entities.Company;
import com.pts.common.entities.Operator;
import com.pts.common.entities.ParkingSlot;
import org.fsociety.identityservice.config.MongoDBConfiguration;

import javax.annotation.Resource;

public abstract class AbstractMongoBaseDAO {

    protected final static String PARKING_SLOT_COLLECTION_NAME = "ParkingSlots";
    protected final static String OPERATOR_COLLECTION_NAME = "Operators";
    protected final static String COMPANY_COLLECTION_NAME = "Companies";
    protected final static String BUILDING_COLLECTION_NAME = "Buildings";

    @Resource(name = MongoDBConfiguration.MONGO_DATABASE_BEAN_IDENTIFIER)
    protected MongoDatabase mongoDatabase;

    protected MongoCollection getParkingSlotCollection() {
        return getMongoCollection(PARKING_SLOT_COLLECTION_NAME, ParkingSlot.class);
    }

    protected MongoCollection getCompanyCollection() {
        return getMongoCollection(COMPANY_COLLECTION_NAME, Company.class);
    }

    protected MongoCollection getOperatorCollection() {
        return getMongoCollection(OPERATOR_COLLECTION_NAME, Operator.class);
    }

    protected MongoCollection getBuildingCollection() {
        return getMongoCollection(BUILDING_COLLECTION_NAME, Building.class);
    }

    private MongoCollection getMongoCollection(final String collectionName, Class collectionClassName) {
        return mongoDatabase.getCollection(collectionName, collectionClassName);
    }
}
