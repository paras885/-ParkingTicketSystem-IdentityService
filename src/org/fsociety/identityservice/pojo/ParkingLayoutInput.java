package org.fsociety.identityservice.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
public class ParkingLayoutInput {

    private String buildingId;
    private MultipartFile multipartFile;
}
