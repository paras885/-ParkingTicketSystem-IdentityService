package org.fsociety.identityservice.businesslogic.impl;

import com.pts.common.entities.ParkingSlot;
import org.fsociety.identityservice.businesslogic.ParkingSlotBusinessLogic;
import org.fsociety.identityservice.dao.BaseCRUDDAO;
import org.fsociety.identityservice.dao.ParkingSlotDAO;
import org.fsociety.identityservice.dao.impl.ParkingSlotDAOImpl;
import org.fsociety.identityservice.exception.BusinessLogicRetryableException;
import org.fsociety.identityservice.pojo.ParkingSlotSearchInput;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component(value = ParkingSlotBusinessLogicImpl.BEAN_IDENTIFIER)
public class ParkingSlotBusinessLogicImpl extends AbstractBaseCRUDBusinessLogic<ParkingSlot>
    implements ParkingSlotBusinessLogic {

    public final static String BEAN_IDENTIFIER = "parkingSlotBusinessLogicImpl";

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
    protected BaseCRUDDAO<ParkingSlot> getResourceDAO() {
        return parkingSlotDAO;
    }
}
