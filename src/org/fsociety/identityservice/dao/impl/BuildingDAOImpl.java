package org.fsociety.identityservice.dao.impl;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.pts.common.entities.Building;
import org.bson.conversions.Bson;
import org.fsociety.identityservice.dao.BuildingDAO;
import org.springframework.stereotype.Component;

import java.util.List;

@Component(value = BuildingDAOImpl.BEAN_IDENTIFIER)
public class BuildingDAOImpl extends AbstractBaseCRUDDAOImpl<Building> implements BuildingDAO {

    public final static String BEAN_IDENTIFIER = "buildingDAOImpl";

    private final static String BUILDING_ID_FIELD_IDENTIFIER = "buildingId";
    private final static String COMPANY_IDS_FIELD_IDENTIFIER = "companyIds";
    private final static String PARKING_SLOT_IDS_FIELD_IDENTIFIER = "parkingSlotIds";

    @Override
    public Building updateCompaniesInBuilding(final String buildingId, final List<String> companyIds) {
        return updateArrayField(buildingId, COMPANY_IDS_FIELD_IDENTIFIER, companyIds);
    }

    @Override
    public Building updateParkingSlotsInBuilding(final String buildingId, final List<String> parkingSlotIds) {
        return updateArrayField(buildingId, PARKING_SLOT_IDS_FIELD_IDENTIFIER, parkingSlotIds);
    }

    private Building updateArrayField(final String buildingId, final String fieldName, final List values) {
        final MongoCollection buildingCollection = getBuildingCollection();
        final Bson searchQuery = Filters.eq(BUILDING_ID_FIELD_IDENTIFIER, buildingId);
        buildingCollection.updateOne(
            searchQuery,
            Updates.addEachToSet(fieldName, values)
        );

        return (Building) buildingCollection.find(searchQuery).first();
    }

    @Override
    protected MongoCollection getResourceCollection() {
        return getBuildingCollection();
    }

    @Override
    protected String getResourceId(final Building building) {
        return building.getBuildingId();
    }

    @Override
    protected String getResourceIdFieldIdentifier() {
        return BUILDING_ID_FIELD_IDENTIFIER;
    }

    @Override
    protected void setResourceId(final Building building, final String buildingId) {
        building.setBuildingId(buildingId);
    }
}
