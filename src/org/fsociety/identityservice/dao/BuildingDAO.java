package org.fsociety.identityservice.dao;

import com.pts.common.entities.Building;

import java.util.List;

public interface BuildingDAO extends BaseCRUDDAO<Building> {

    Building updateCompaniesInBuilding(final String buildingId, final List<String> companyIds);

    Building updateParkingSlotsInBuilding(final String buildingId, final List<String> parkingSlotIds);
}
