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

    /**
     * @param requirementsForSlot is an instance of {@link ParkingSlot} which hold requirements for eligible parking slot.
     * @return output is also an instance of {@link ParkingSlot} which hold parkingSlotId and vacantStatus will be PRERESERVATION.
     * @throws BusinessLogicRetryableException If in 3 tries we are not able to reserve any slot ask for fresh request.
     * @throws BusinessLogicNonRetryableException No slot available.
     */
    ParkingSlot preReserveParkingSlot(final ParkingSlot requirementsForSlot)
        throws BusinessLogicNonRetryableException, BusinessLogicRetryableException;
}
