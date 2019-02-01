package org.fsociety.identityservice.businessobject;

import com.pts.common.entities.ParkingSlot;
import com.pts.common.entities.ParkingSlotVacantStatus;
import com.pts.common.entities.VehicleType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.fsociety.identityservice.exception.ParkingLayoutFileValidationException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
public class ParkingLayoutDetails {

    private final static String SPLIT_DELIMITER_BY_COMMA = ",";
    private final static List<String> expectedHeaders = Arrays.asList("companyId", "floorNumber", "slotNumber",
        "isReserved", "vehicleType");

    private String buildingId;
    private List<ParkingSlot> parkingSlots;
    private Set<String> distinctCompanyIds;

    public static ParkingLayoutDetails of(final String buildingId) {
        final ParkingLayoutDetails details = ParkingLayoutDetails.builder()
            .buildingId(buildingId)
            .build();
        details.initialize();

        return details;
    }

    private void initialize() {
        parkingSlots = new ArrayList<>();
        distinctCompanyIds = new HashSet<>();
    }

    public void validateHeaders(final String firstRowFromFile) throws ParkingLayoutFileValidationException {
        final String[] actualHeaders = firstRowFromFile.split(SPLIT_DELIMITER_BY_COMMA);
        if (actualHeaders.length != expectedHeaders.size()) {
            throw new ParkingLayoutFileValidationException("Required headers are not present in file.");
        }

        for (int counterForCurrentHeader = 0; counterForCurrentHeader < actualHeaders.length;
             ++counterForCurrentHeader) {
            final String expectedHeader = expectedHeaders.get(counterForCurrentHeader);
            final String actualHeader = actualHeaders[counterForCurrentHeader].trim();
            if (expectedHeader.compareTo(actualHeader) != 0) {
                final String errorMessage = String.format("ExpectedHeader is %s but found %s", expectedHeader,
                    actualHeader);
                throw new ParkingLayoutFileValidationException(errorMessage);
            }
        }
    }

    /*
     *  TODO: Need a way to map ParkingSlot pojo by using expectedHeaders list.
     *  and validate provided information e.g: companyId is valid or not.
     *  Note : [tatti kri h is method me saaf krni hogi]
    */
    public void updateParkingSlots(final String rowFromFile) throws ParkingLayoutFileValidationException {
        final String[] valuesOfRow = rowFromFile.split(SPLIT_DELIMITER_BY_COMMA);
        if (valuesOfRow.length != expectedHeaders.size()) {
            final String errorMessage = "Required attributes are not present in file for parking slot.";
            throw new ParkingLayoutFileValidationException(errorMessage);
        }

        final String companyId = valuesOfRow[0].trim();
        final String floorNumber = valuesOfRow[1].trim();
        final String slotNumber = valuesOfRow[2].trim();
        final Boolean isReserved = Boolean.valueOf(valuesOfRow[3].trim());
        final VehicleType vehicleType = VehicleType.valueOf(valuesOfRow[4].trim());
        final ParkingSlot parkingSlot = ParkingSlot.builder()
            .buildingId(buildingId)
            .companyId(companyId)
            .floorNumber(floorNumber)
            .slotNumber(slotNumber)
            .isReserved(isReserved)
            .vehicleType(vehicleType)
            .vacantStatus(ParkingSlotVacantStatus.EMPTY)
            .build();

        distinctCompanyIds.add(companyId);
        parkingSlots.add(parkingSlot);
    }
}
