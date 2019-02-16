package org.fsociety.identityservice.businesslogic.impl;

import com.pts.common.entities.ParkingSlot;
import com.pts.common.entities.ParkingSlotVacantStatus;
import lombok.extern.slf4j.Slf4j;
import org.fsociety.identityservice.businesslogic.ParkingSlotBusinessLogic;
import org.fsociety.identityservice.dao.BaseCRUDDAO;
import org.fsociety.identityservice.dao.ParkingSlotDAO;
import org.fsociety.identityservice.dao.impl.ParkingSlotDAOImpl;
import org.fsociety.identityservice.exception.BusinessLogicNonRetryableException;
import org.fsociety.identityservice.exception.BusinessLogicRetryableException;
import org.fsociety.identityservice.exception.DAONonRetryableException;
import org.fsociety.identityservice.exception.DAORetryableException;
import org.fsociety.identityservice.pojo.ParkingSlotSearchInput;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Component(value = ParkingSlotBusinessLogicImpl.BEAN_IDENTIFIER)
public class ParkingSlotBusinessLogicImpl extends AbstractBaseCRUDBusinessLogic<ParkingSlot>
    implements ParkingSlotBusinessLogic {

    public final static String BEAN_IDENTIFIER = "parkingSlotBusinessLogicImpl";

    private final static String EMPTY_PARKING_SLOT_VALUE = "EMPTY";
    private final static int RETRY_DELAY_IN_MILISECONDS = 1000;

    @Resource(name = ParkingSlotDAOImpl.BEAN_IDENTIFIER)
    private ParkingSlotDAO parkingSlotDAO;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ParkingSlot> getParkingSlotsBySearchInput(ParkingSlotSearchInput input)
        throws BusinessLogicRetryableException {
            return parkingSlotDAO.fetchParkingSlotsBySearchInput(input);
    }

    @Override
    public ParkingSlot preReserveParkingSlot(final ParkingSlot requirementsForSlot)
        throws BusinessLogicNonRetryableException, BusinessLogicRetryableException {
            try {
                final ParkingSlotSearchInput searchInput = createSearchInputForFreeSlot(requirementsForSlot);
                return preReserve(searchInput);
            } catch (final DAORetryableException daoRetryException) {
                log.error("Not able to preReserve slot for requirements: {}", requirementsForSlot);
                throw new BusinessLogicRetryableException("Failed to preReserve parkingSlot", daoRetryException);
            }
    }

    /* TODO: remove hard coded strings and get value from method invocation using reflection */
    private ParkingSlotSearchInput createSearchInputForFreeSlot(final ParkingSlot requirementsForSlot) {
        final Map<String, String> searchParams = new HashMap<>();
        searchParams.put("companyId", requirementsForSlot.getCompanyId());
        searchParams.put("vehicleType", requirementsForSlot.getVehicleType().toString());
        searchParams.put("vacantStatus", EMPTY_PARKING_SLOT_VALUE);
        searchParams.put("isReserved", requirementsForSlot.getIsReserved().toString());
        searchParams.put("limit", "1");

        return ParkingSlotSearchInput.builder()
            .buildingId(requirementsForSlot.getBuildingId())
            .searchParams(searchParams)
            .build();
    }

    @Retryable(value = {DAORetryableException.class}, maxAttempts = 3, backoff = @Backoff(delay = RETRY_DELAY_IN_MILISECONDS))
    private ParkingSlot preReserve(final ParkingSlotSearchInput searchInput)
        throws BusinessLogicNonRetryableException, DAORetryableException {
            final ParkingSlot freeSlot = fetchFreeParkingSlot(searchInput);
            parkingSlotDAO.preReserveParkingSlot(freeSlot);

            return freeSlot;
    }

    private ParkingSlot fetchFreeParkingSlot(final ParkingSlotSearchInput searchInput)
        throws BusinessLogicNonRetryableException {
            final List<ParkingSlot> freeSlot = parkingSlotDAO.fetchParkingSlotsBySearchInput(searchInput);
            if (Objects.isNull(freeSlot) || freeSlot.size() == 0) {
                throw new BusinessLogicNonRetryableException("No free slot available.");
            } else {
                return freeSlot.get(0);
            }
    }

    @Override
    public Optional<ParkingSlot> vacantParkingSlot(final ParkingSlot vacantSlotRequest) {
        try {
            parkingSlotDAO.vacantParkingSlot(vacantSlotRequest);
            return Optional.of(vacantSlotRequest);
        } catch (final DAONonRetryableException exception) {
            return Optional.empty();
        }
    }

    @Override
    protected BaseCRUDDAO<ParkingSlot> getResourceDAO() {
        return parkingSlotDAO;
    }
}
