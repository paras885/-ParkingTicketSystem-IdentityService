package org.fsociety.identityservice.dao.impl;

import com.mongodb.Block;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.InsertOneModel;
import com.mongodb.client.model.UpdateOneModel;
import com.mongodb.client.model.Updates;
import com.mongodb.client.model.WriteModel;
import com.pts.common.entities.Company;
import com.pts.common.entities.ParkingSlot;
import lombok.extern.slf4j.Slf4j;
import org.bson.conversions.Bson;
import org.fsociety.identityservice.dao.ParkingSlotDAO;
import org.fsociety.identityservice.pojo.ParkingSlotSearchInput;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Component(value = ParkingSlotDAOImpl.BEAN_IDENTIFIER)
public class ParkingSlotDAOImpl extends AbstractBaseCRUDDAOImpl<ParkingSlot> implements ParkingSlotDAO {

    public final static String BEAN_IDENTIFIER = "ParkingSlotDAOImpl";

    private final static String PARKING_SLOT_ID_FIELD_IDENTIFIER = "parkingSlotId";
    private final static String COMPANY_ID_FIELD_IDENTIFIER = "companyId";
    private final static String BUILDING_ID_FIELD_IDENTIFIER = "buildingId";
    private final static String BUILDING_IDS_FIELD_IDENTIFIER = "buildingIds";

    private final static String LIMIT_IDENTIFIER_IN_SEARCH_PARAM = "limit";

    private final static int BATCH_SIZE = 1000;
    private final static int DEFAULT_DOCUMENT_LIMIT = 100;

    // TODO: Create bulk writer class[using Iterator pattern] and reduce method length.
    @Override
    public List<String> bulkInsert(final List<ParkingSlot> parkingSlots) {
        final List<String> parkingSlotIds = new ArrayList<>();
        final MongoCollection parkingSlotCollection = getParkingSlotCollection();
        final MongoCollection companyCollection = getCompanyCollection();

        List<WriteModel<ParkingSlot>> bulkWritesForParkingSlot = new ArrayList<>();
        List<WriteModel<Company>> bulkUpdatesForCompanies = new ArrayList<>();
        for (final ParkingSlot parkingSlot : parkingSlots) {
            final String parkingSlotId = createParkingSlotId();
            parkingSlotIds.add(parkingSlotId);
            parkingSlot.setParkingSlotId(parkingSlotId);

            bulkWritesForParkingSlot.add(new InsertOneModel<>(parkingSlot));
            bulkUpdatesForCompanies.add(createUpdateQueryOfBuildingForCompany(parkingSlot));
            if (bulkWritesForParkingSlot.size() == BATCH_SIZE) {
                parkingSlotCollection.bulkWrite(bulkWritesForParkingSlot);
                companyCollection.bulkWrite(bulkUpdatesForCompanies);

                bulkWritesForParkingSlot = new ArrayList<>();
                bulkUpdatesForCompanies = new ArrayList<>();
            }
        }

        if (bulkWritesForParkingSlot.size() > 0) {
            parkingSlotCollection.bulkWrite(bulkWritesForParkingSlot);
            companyCollection.bulkWrite(bulkUpdatesForCompanies);
        }

        return parkingSlotIds;
    }

    private String createParkingSlotId() {
        final String uuidInStringForm = UUID.randomUUID().toString();

        return uuidInStringForm.replaceAll("-","");
    }

    private UpdateOneModel createUpdateQueryOfBuildingForCompany(final ParkingSlot parkingSlot) {
        return new UpdateOneModel<Company>(
            Filters.eq(COMPANY_ID_FIELD_IDENTIFIER, parkingSlot.getCompanyId()),
            Updates.addEachToSet(BUILDING_IDS_FIELD_IDENTIFIER, Arrays.asList(parkingSlot.getBuildingId()))
        );
    }

    @Override
    public List<ParkingSlot> fetchParkingSlotsBySearchInput(ParkingSlotSearchInput searchInput) {
        Bson filterQuery = Filters.eq(BUILDING_ID_FIELD_IDENTIFIER, searchInput.getBuildingId());
        int limitFilter = 0;
        for (Map.Entry<String, String> searchParam : searchInput.getSearchParams().entrySet()) {
            final String fieldName = searchParam.getKey();
            final String value = searchParam.getValue();
            if (fieldName.compareTo(LIMIT_IDENTIFIER_IN_SEARCH_PARAM) == 0) {
                limitFilter = Integer.parseInt(value);
            } else {
                filterQuery = Filters.and(filterQuery, Filters.eq(fieldName, value));
            }
        }
        limitFilter = limitFilter == 0 ? DEFAULT_DOCUMENT_LIMIT : limitFilter;

        final List<ParkingSlot> parkingSlots = new ArrayList<>();
        final Block<ParkingSlot> updateParkingSlotsIntoList = new Block<ParkingSlot>() {
            @Override
            public void apply(ParkingSlot parkingSlot) {
                parkingSlots.add(parkingSlot);
            }
        };
        getParkingSlotCollection().find(filterQuery)
            .limit(limitFilter)
            .forEach(updateParkingSlotsIntoList);

        return parkingSlots;
    }

    @Override
    protected MongoCollection getResourceCollection() {
        return getParkingSlotCollection();
    }

    @Override
    protected String getResourceId(final ParkingSlot parkingSlot) {
        return parkingSlot.getParkingSlotId();
    }

    @Override
    protected String getResourceIdFieldIdentifier() {
        return PARKING_SLOT_ID_FIELD_IDENTIFIER;
    }

    @Override
    protected void setResourceId(final ParkingSlot parkingSlot, final String parkingSlotId) {
        parkingSlot.setParkingSlotId(parkingSlotId);
    }
}
