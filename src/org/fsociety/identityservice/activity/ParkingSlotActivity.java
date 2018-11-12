package org.fsociety.identityservice.activity;

import com.pts.common.entities.ParkingSlot;
import lombok.extern.slf4j.Slf4j;
import org.fsociety.identityservice.businesslogic.ParkingSlotBusinessLogic;
import org.fsociety.identityservice.businesslogic.impl.ParkingSlotBusinessLogicImpl;
import org.fsociety.identityservice.exception.BusinessLogicRetryableException;
import org.fsociety.identityservice.pojo.ParkingSlotSearchInput;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.fsociety.identityservice.activity.BuildingActivity.BUILDING_RESOURCE_URL;

@Slf4j
@RequestMapping(value = BUILDING_RESOURCE_URL + "/{buildingId}" + ParkingSlotActivity.PARKING_SLOT_RESOURCE_URL)
@RestController
public class ParkingSlotActivity extends AbstractBaseCRUDActivity<ParkingSlot> {

    public final static String PARKING_SLOT_RESOURCE_URL = "/parkingSlots";

    private final static List<String> SUPPORTED_REQUEST_PARAMS_LIST_FOR_PARKING_SLOTS = Arrays.asList(
        "companyId", "parkingSlotId", "vehicleType", "isReserved", "isVacant", "floorNumber", "limit");
    private final static Set<String> SUPPORTED_REQUEST_PARAMS_SET_FOR_PARKING_SLOTS =
        new HashSet<>(SUPPORTED_REQUEST_PARAMS_LIST_FOR_PARKING_SLOTS);

    @Resource(name = ParkingSlotBusinessLogicImpl.BEAN_IDENTIFIER)
    private ParkingSlotBusinessLogic businessLogic;

    @GetMapping(name = "searchQueryForParkingSlotsOfBuilding", value = "/searchParams")
    @ResponseBody
    public ResponseEntity<List<ParkingSlot>> getParkingSlotsByBuildingIdAndSearchParams(
        @PathVariable final String buildingId, final HttpServletRequest servletRequest) {
            ResponseEntity<List<ParkingSlot>> response = null;

            try {
                final Map<String, String> searchParams = convertIntoParkingSearchParams(
                    servletRequest.getParameterMap());
                final boolean isValidParams = validateRequestParams(SUPPORTED_REQUEST_PARAMS_SET_FOR_PARKING_SLOTS,
                    searchParams.keySet());
                if (isValidParams) {
                    final ParkingSlotSearchInput searchInput = ParkingSlotSearchInput.builder()
                        .buildingId(buildingId)
                        .searchParams(searchParams)
                        .build();
                    log.info("getParkingSlotsByBuildingIdAndSearchParams invoked where searchInput is : {}",
                        searchInput);

                    response = new ResponseEntity<List<ParkingSlot>>(
                        businessLogic.getParkingSlotsBySearchInput(searchInput), HttpStatus.OK);
                } else {
                    response = new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
                }
            } catch (final BusinessLogicRetryableException businessLogicException) {
                response = new ResponseEntity<>(null, HttpStatus.FAILED_DEPENDENCY);
            }

            return response;
    }

    /*
     * Right now we are accessing first element from each parameter because we are expecting only one
     * value in search param.
     *
     * TODO: Need to implement in a way so we can handle multiple values.
     */
    private Map<String, String> convertIntoParkingSearchParams(final Map<String, String[]> requestParameterMap) {
        log.info("convertIntoParkingSearchParams invoked with requestParameterMap: {}", requestParameterMap);
        final Map<String, String> searchParams = new HashMap<>();
        for (Map.Entry<String, String[]> entry : requestParameterMap.entrySet()) {
            searchParams.put(entry.getKey(), entry.getValue()[0]);
        }

        return searchParams;
    }

    private boolean validateRequestParams(final Set<String> supportedParams, final Set<String> receivedParams) {
        return (receivedParams.stream()
            .filter(receivedParam -> supportedParams.contains(receivedParam))
            .count()) > 0;
    }

    @Override
    public ParkingSlotBusinessLogic getBusinessLogic() {
        return businessLogic;
    }
}
