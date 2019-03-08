package org.fsociety.identityservice.businesslogic;

import com.pts.common.entities.ParkingSlot;
import org.fsociety.identityservice.exception.BusinessLogicNonRetryableException;
import org.fsociety.identityservice.exception.BusinessLogicRetryableException;
import org.fsociety.identityservice.pojo.ParkingSlotSearchInput;

import java.util.List;

public interface ParkingSlotBusinessLogic extends BaseBusinessLogic<ParkingSlot> {

    /**
     * @param input is an instance of {@link ParkingSlotSearchInput} which hold building id and search params.
     * @return List of parking slots which comes under provided building id and satisfy parameters.
     * @throws BusinessLogicRetryableException which can occur because of mongodb failure.
     */
    List<ParkingSlot> getParkingSlotsBySearchInput(final ParkingSlotSearchInput input)
            throws BusinessLogicRetryableException;

    ParkingSlot updateParkingSlot(final ParkingSlot updatedParkingSlot)
            throws BusinessLogicNonRetryableException, BusinessLogicRetryableException;
}
