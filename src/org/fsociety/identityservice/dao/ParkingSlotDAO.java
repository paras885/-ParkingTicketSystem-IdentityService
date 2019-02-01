package org.fsociety.identityservice.dao;

import com.pts.common.entities.ParkingSlot;
import org.fsociety.identityservice.exception.DAORetryableException;
import org.fsociety.identityservice.pojo.ParkingSlotSearchInput;

import java.util.List;

public interface ParkingSlotDAO extends BaseCRUDDAO<ParkingSlot> {

    List<String> bulkInsert(final List<ParkingSlot> parkingSlots);

    List<ParkingSlot> fetchParkingSlotsBySearchInput(final ParkingSlotSearchInput searchInput);

    void preReserveParkingSlot(final ParkingSlot parkingSlot) throws DAORetryableException;
}
