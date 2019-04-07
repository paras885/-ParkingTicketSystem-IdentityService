package org.fsociety.identityservice.activity;

import com.pts.common.entities.Building;
import lombok.extern.slf4j.Slf4j;
import org.fsociety.identityservice.businesslogic.BuildingBusinessLogic;
import org.fsociety.identityservice.businesslogic.impl.BuildingBusinessLogicImpl;
import org.fsociety.identityservice.exception.BusinessLogicNonRetryableException;
import org.fsociety.identityservice.pojo.ParkingLayoutInput;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

import static org.fsociety.identityservice.activity.ParkingSlotActivity.PARKING_SLOT_RESOURCE_URL;

@Slf4j
@RequestMapping(value = BuildingActivity.BUILDING_RESOURCE_URL)
@RestController
public class BuildingActivity extends AbstractBaseCRUDActivity<Building> {

    public final static String BUILDING_RESOURCE_URL = "/buildings";

    private final static String UPLOAD_PARKING_LAYOUT_API_URL = "/{buildingId}" + PARKING_SLOT_RESOURCE_URL;
    private final static String SEARCH_PARKING_SLOT_BY_QUERY_FILTER = "/{buildingId}" + PARKING_SLOT_RESOURCE_URL;

    @Resource(name = BuildingBusinessLogicImpl.BEAN_IDENTIFIER)
    private BuildingBusinessLogic businessLogic;

    @PostMapping(name = "uploadParkingLayout", value = UPLOAD_PARKING_LAYOUT_API_URL + "/uploadFile")
    @ResponseBody
    public ResponseEntity<Building> uploadParkingLayout(@PathVariable final String buildingId,
                                                        @RequestParam("file") final MultipartFile multipartFile) {
        ResponseEntity<Building> response = null;
        try {
            final ParkingLayoutInput input = ParkingLayoutInput.builder()
                .buildingId(buildingId)
                .multipartFile(multipartFile)
                .build();
            log.info("uploadParkingLayout invoked with parkingLayoutInput : {}", input);

            response = new ResponseEntity<Building>(businessLogic.saveParkingLayout(input), HttpStatus.OK);
        } catch (final BusinessLogicNonRetryableException businessException) {
            response = new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

        return response;
    }

    @Override
    public BuildingBusinessLogic getBusinessLogic() {
        return businessLogic;
    }
}
