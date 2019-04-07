package org.fsociety.identityservice.activity;

import lombok.extern.slf4j.Slf4j;
import org.fsociety.identityservice.businesslogic.BaseBusinessLogic;
import org.fsociety.identityservice.exception.BusinessLogicNonRetryableException;
import org.fsociety.identityservice.exception.BusinessLogicRetryableException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
public abstract class AbstractBaseCRUDActivity<ResourceType> {

    /**
     * @param resource is an instance of {@link ResourceType}.
     * @return <StatusCode, Company> 2xx for ok or 4xx for exception[e.g 424 dependency failure].
     */
    @PostMapping(name = "addNewResource")
    @ResponseBody
    public ResponseEntity<ResourceType> addResource(final @RequestBody ResourceType resource) {
        log.info("Request to add resource is : {}", resource);

        ResponseEntity<ResourceType> response = null;
        try {
            response = new ResponseEntity<ResourceType>(getBusinessLogic().addResource(resource), HttpStatus.OK);
            log.info("Resource : {} added successfully.", resource);
        } catch (final BusinessLogicRetryableException exception) {
            log.error("Error occurred when trying to add resource : {}, exception", resource, exception);
            response = new ResponseEntity<>(null, HttpStatus.FAILED_DEPENDENCY);
        }

        return response;
    }

    protected abstract BaseBusinessLogic<ResourceType> getBusinessLogic();

    /**
     * @param resourceId From database it will resource whose resourceId will be equal to this.
     * @return <StatusCode, Company> 2xx for ok or 4xx for exception[e.g 404 not found].
     */
    @GetMapping(name = "getResourceByResourceId", value = "/{resourceId}")
    @ResponseBody
    public ResponseEntity<ResourceType> getResource(final @PathVariable String resourceId) {
        log.info("Request to fetch resource where resourceId is : {}", resourceId);

        ResponseEntity<ResourceType> response = null;
        try {
            final ResourceType resource = getBusinessLogic().getResource(resourceId);
            log.info("For resourceId : {}, fetched Resource is : {}", resourceId, resource);
            response = new ResponseEntity<ResourceType>(resource, HttpStatus.OK);
        } catch (final BusinessLogicNonRetryableException exception) {
            log.error("Error occurred when trying to fetch Resource for resourceId : {}, exception", resourceId,
                exception);
            response = new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        return response;
    }

    /**
     * @param resourceId It will be helpful to fetch old record.
     * @param resource This will hold new values which need to be Update/PUT.
     * @return <StatusCode, Company> 2xx for ok or 4xx for exception[e.g 400 for bad request].
     */
    @PutMapping(name = "updateResource", value = "/{resourceId}")
    @ResponseBody
    public ResponseEntity<ResourceType> updateResource(final @PathVariable String resourceId,
                                                       final @RequestBody ResourceType resource) {
        log.info("Request to Update/PUT resource where resourceId is : {} and resource is : {}", resourceId, resource);

        ResponseEntity<ResourceType> response = null;
        try {
            response = new ResponseEntity<ResourceType>(getBusinessLogic().updateResource(resourceId, resource),
                HttpStatus.OK);
            log.info("For resourceId: {} resource updated with these values : {}", resourceId, resource);
        } catch (final BusinessLogicNonRetryableException exception) {
            final String logMessage =
                "Error occurred when tried to put new value where resourceId : {} and resource : {}, exception";
            log.error(String.format(logMessage, resourceId, resource), exception);
            response = new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

        return response;
    }

    /**
     * @param resourceId For which we need to delete resource.
     * @return <StatusCode, Message> 2xx for ok or 4xx for exception[e.g 424 dependency failure].
     */
    @DeleteMapping(name = "deleteCompany", value = "/{resourceId}")
    @ResponseBody
    public ResponseEntity<String> deleteResource(final @PathVariable String resourceId) {
        log.info("Request to delete resource where resourceId is : {}", resourceId);

        ResponseEntity<String> response = null;
        try {
            getBusinessLogic().deleteResource(resourceId);
            response = new ResponseEntity<String>("Successfully deleted resource", HttpStatus.OK);
        } catch (final BusinessLogicRetryableException exception) {
            final String errorMessage =
                String.format("Error occurred when tried to delete resource where resourceId : %s", resourceId);
            log.error(errorMessage, exception);
            response = new ResponseEntity<String>(errorMessage, HttpStatus.FAILED_DEPENDENCY);
        }

        return response;
    }
}
