package org.fsociety.identityservice.businesslogic.impl;

import com.pts.common.entities.Building;
import lombok.extern.slf4j.Slf4j;
import org.fsociety.identityservice.businesslogic.BuildingBusinessLogic;
import org.fsociety.identityservice.businessobject.ParkingLayoutDetails;
import org.fsociety.identityservice.dao.BaseCRUDDAO;
import org.fsociety.identityservice.dao.BuildingDAO;
import org.fsociety.identityservice.dao.ParkingSlotDAO;
import org.fsociety.identityservice.dao.impl.BuildingDAOImpl;
import org.fsociety.identityservice.dao.impl.ParkingSlotDAOImpl;
import org.fsociety.identityservice.exception.BusinessLogicNonRetryableException;
import org.fsociety.identityservice.exception.ParserNonRetryableException;
import org.fsociety.identityservice.lib.parser.Parser;
import org.fsociety.identityservice.lib.parser.impl.ParkingLayoutParser;
import org.fsociety.identityservice.pojo.ParkingLayoutInput;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component(value = BuildingBusinessLogicImpl.BEAN_IDENTIFIER)
public class BuildingBusinessLogicImpl extends AbstractBaseCRUDBusinessLogic<Building>
    implements BuildingBusinessLogic {

    public final static String BEAN_IDENTIFIER = "buildingBusinessLogicImpl";

    @Resource(name = ParkingLayoutParser.BEAN_IDENTIFIER)
    private Parser<ParkingLayoutDetails, ParkingLayoutInput> parser;

    @Resource(name = ParkingSlotDAOImpl.BEAN_IDENTIFIER)
    private ParkingSlotDAO parkingSlotDAO;

    @Resource(name = BuildingDAOImpl.BEAN_IDENTIFIER)
    private BuildingDAO buildingDAO;

    /**
     * {@inheritDoc}
     */
    @Override
    public Building saveParkingLayout(final ParkingLayoutInput input) throws BusinessLogicNonRetryableException {
        try {
            checkExistenceForBuilding(input.getBuildingId());

            final ParkingLayoutDetails layoutDetails = parser.parse(input);
            final Building updatedBuilding = updateBuildingParkingLayoutDetails(layoutDetails);

            return updatedBuilding;
        } catch (final ParserNonRetryableException parserException) {
            log.error("Failed to parse parkingLayout for input : {}", input, parserException);
            throw new BusinessLogicNonRetryableException("Exception occurred during parsing.", parserException);
        }
    }

    /*
      TODO: Need to find better way to for this check or create transactional operation for caller method.
     */
    private void checkExistenceForBuilding(final String buildingId) throws BusinessLogicNonRetryableException {
        getResource(buildingId); // It will throw exception if resource is not present
        log.info("Building exists for buildingId: {}", buildingId);
    }

    private Building updateBuildingParkingLayoutDetails(final ParkingLayoutDetails layoutDetails) {
        updateCompanyDetails(layoutDetails);
        final Building updatedBuilding = updateParkingSlots(layoutDetails);

        return updatedBuilding;
    }

    /*
      TODO: Currently there is no validation on companyId we need to figure out a way to check if all these ids are
            valid or not.
     */
    private Building updateCompanyDetails(final ParkingLayoutDetails layoutDetails) {
        return buildingDAO.updateCompaniesInBuilding(layoutDetails.getBuildingId(),
            new ArrayList<>(layoutDetails.getDistinctCompanyIds()));
    }

    private Building updateParkingSlots(final ParkingLayoutDetails layoutDetails) {
        final List<String> insertedParkingSlotIds = parkingSlotDAO.bulkInsert(layoutDetails.getParkingSlots());
        return buildingDAO.updateParkingSlotsInBuilding(layoutDetails.getBuildingId(), insertedParkingSlotIds);
    }

    @Override
    protected BaseCRUDDAO<Building> getResourceDAO() {
        return buildingDAO;
    }
}
