package org.fsociety.identityservice.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Map;

@AllArgsConstructor
@Builder
@Data
public class ParkingSlotSearchInput {

    private String buildingId;
    private Map<String, String> searchParams;
}
