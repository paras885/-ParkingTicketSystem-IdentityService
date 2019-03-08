package org.fsociety.identityservice.businesslogic.impl;

import com.pts.common.entities.ParkingSlot;
import lombok.extern.slf4j.Slf4j;
import org.fsociety.identityservice.businesslogic.ParkingSlotBusinessLogic;
import org.fsociety.identityservice.dao.BaseCRUDDAO;
import org.fsociety.identityservice.dao.ParkingSlotDAO;
import org.fsociety.identityservice.dao.impl.ParkingSlotDAOImpl;
import org.fsociety.identityservice.exception.BusinessLogicNonRetryableException;
import org.fsociety.identityservice.exception.BusinessLogicRetryableException;
import org.fsociety.identityservice.exception.DAONonRetryableException;
import org.fsociety.identityservice.pojo.ParkingSlotSearchInput;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component(value = ParkingSlotBusinessLogicImpl.BEAN_IDENTIFIER)
public class ParkingSlotBusinessLogicImpl extends AbstractBaseCRUDBusinessLogic<ParkingSlot>
    implements ParkingSlotBusinessLogic {

    public final static String BEAN_IDENTIFIER = "parkingSlotBusinessLogicImpl";

    private final static String PARKING_SLOT_ID_KEY = "parkingSlotId";

    @Resource(name = ParkingSlotDAOImpl.BEAN_IDENTIFIER)
    private ParkingSlotDAO parkingSlotDAO;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ParkingSlot> getParkingSlotsBySearchInput(final ParkingSlotSearchInput input)
        throws BusinessLogicRetryableException {
            return parkingSlotDAO.fetchParkingSlotsBySearchInput(input);
    }

    // Right now, We are only supporting value change for vacant status
    @Override
    public ParkingSlot updateParkingSlot(final ParkingSlot updatedParkingSlot)
            throws BusinessLogicNonRetryableException, BusinessLogicRetryableException {
        final ParkingSlotSearchInput slotSearchInput = createSearchInputForSlot(updatedParkingSlot);
        final List<ParkingSlot> parkingSlots = parkingSlotDAO.fetchParkingSlotsBySearchInput(slotSearchInput);
        if (CollectionUtils.isEmpty(parkingSlots)) {
            final String errorMessage = String.format("No ParkingSlot present for given searchInput: {}",
                    slotSearchInput);
            throw new BusinessLogicNonRetryableException(errorMessage);
        } else {
            final ParkingSlot currentStateOfSlot = extractFirstSlot(parkingSlots);
            updateParkingSlot(currentStateOfSlot, updatedParkingSlot);
        }

        return updatedParkingSlot;
    }

    /* TODO: remove hard coded strings and get value from method invocation using reflection */
    private ParkingSlotSearchInput createSearchInputForSlot(final ParkingSlot requirementsForSlot) {
        final Map<String, String> searchParams = new HashMap<>();
        searchParams.put(PARKING_SLOT_ID_KEY, requirementsForSlot.getParkingSlotId());

        return ParkingSlotSearchInput.builder()
            .buildingId(requirementsForSlot.getBuildingId())
            .searchParams(searchParams)
            .build();
    }

    private void updateParkingSlot(final ParkingSlot currentStateOfSlot, final ParkingSlot requiredStateOfSlot)
            throws BusinessLogicNonRetryableException, BusinessLogicRetryableException {
        try {
            if (isUpdateFeasible(currentStateOfSlot, requiredStateOfSlot)) {
                parkingSlotDAO.update(requiredStateOfSlot.getParkingSlotId(), requiredStateOfSlot);
            } else {
                final String errorMessage = String.format("Slot update is not feasible, where currentSlot: {} and futureSlot: {}",
                        currentStateOfSlot, requiredStateOfSlot);
                throw new BusinessLogicRetryableException(errorMessage);
            }
        } catch (DAONonRetryableException daoException) {
            final String errorMessage = String.format("Failed to update slot details : {}", requiredStateOfSlot);
            throw new BusinessLogicNonRetryableException(errorMessage, daoException);
        }
    }

    private ParkingSlot extractFirstSlot(final List<ParkingSlot> parkingSlots) {
        return parkingSlots.get(0); // Not good way but right now we don't have any choice.
    }

    private boolean isUpdateFeasible(final ParkingSlot currentState, final ParkingSlot requiredState) {
        switch (currentState.getVacantStatus()) {
            case EMPTY:
                switch (requiredState.getVacantStatus()) {
                    case EMPTY:
                    case PRE_RESERVATION:
                        return true;
                    case RESERVED:
                        return false;
                }
            case PRE_RESERVATION:
                switch (requiredState.getVacantStatus()) {
                    case EMPTY:
                        return true;
                    case RESERVED:
                    case PRE_RESERVATION:
                        return currentState.getParkedVehicleNumber().equals(requiredState.getParkedVehicleNumber());
                }
            case RESERVED:
                switch (requiredState.getVacantStatus()) {
                    case EMPTY:
                        return true;
                    case PRE_RESERVATION:
                        return false;
                    case RESERVED:
                        return currentState.getParkedVehicleNumber().equals(requiredState.getParkedVehicleNumber());
                }
            default:
                return false;
        }
    }

    @Override
    protected BaseCRUDDAO<ParkingSlot> getResourceDAO() {
        return parkingSlotDAO;
    }
}
