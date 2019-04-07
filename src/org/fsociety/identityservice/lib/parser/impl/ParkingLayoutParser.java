package org.fsociety.identityservice.lib.parser.impl;

import lombok.extern.slf4j.Slf4j;
import org.fsociety.identityservice.exception.ParkingLayoutFileValidationException;
import org.fsociety.identityservice.exception.ParserNonRetryableException;
import org.fsociety.identityservice.lib.parser.Parser;
import org.fsociety.identityservice.businessobject.ParkingLayoutDetails;
import org.fsociety.identityservice.pojo.ParkingLayoutInput;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Slf4j
@Component(value = ParkingLayoutParser.BEAN_IDENTIFIER)
public class ParkingLayoutParser implements Parser<ParkingLayoutDetails, ParkingLayoutInput> {

    public final static String BEAN_IDENTIFIER = "parkingLayoutParser";

    @Override
    public ParkingLayoutDetails parse(final ParkingLayoutInput input) throws ParserNonRetryableException {
        try {
            return parse(input.getBuildingId(), input.getMultipartFile());
        } catch (final IOException ioException) {
            final String logMessage = String.format("Error occurred during file reading, when buildingId is %s",
                input.getBuildingId());
            log.error(logMessage, ioException);
            throw new ParserNonRetryableException(logMessage, ioException);
        }
    }

    private ParkingLayoutDetails parse(final String buildingId, final MultipartFile file) throws
        IOException, ParserNonRetryableException {
            try(final BufferedReader bufferedReader = convertIntoBufferedReader(file)) {
                final ParkingLayoutDetails details = ParkingLayoutDetails.of(buildingId);

                String currentLine = bufferedReader.readLine();
                currentLine = currentLine.trim();
                details.validateHeaders(currentLine);
                while ((currentLine = bufferedReader.readLine()) != null) {
                    currentLine = currentLine.trim();
                    details.updateParkingSlots(currentLine);
                }

                return details;
            } catch (final ParkingLayoutFileValidationException validationException) {
                throw new ParserNonRetryableException("Exception occurred during file parsing.", validationException);
            }
    }

    private BufferedReader convertIntoBufferedReader(final MultipartFile file) throws IOException {
        return new BufferedReader(
            new InputStreamReader(file.getInputStream())
        );
    }
}
