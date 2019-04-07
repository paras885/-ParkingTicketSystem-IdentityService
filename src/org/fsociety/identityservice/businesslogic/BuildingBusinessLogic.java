package org.fsociety.identityservice.businesslogic;

import com.pts.common.entities.Building;
import org.fsociety.identityservice.exception.BusinessLogicNonRetryableException;
import org.fsociety.identityservice.pojo.ParkingLayoutInput;

public interface BuildingBusinessLogic extends BaseBusinessLogic<Building> {

    /**
     * @param input is an instance of {@link ParkingLayoutInput} which hold buildingId and layout.
     * @return An instance of {@link Building} with updated ParkingSlotIds and CompanyIds which saved by this operation.
     * @throws BusinessLogicNonRetryableException which can occur because of invalid file.
     */
    Building saveParkingLayout(final ParkingLayoutInput input) throws BusinessLogicNonRetryableException;
}
