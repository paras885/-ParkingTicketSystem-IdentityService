package org.fsociety.identityservice.businesslogic;

import org.fsociety.identityservice.exception.BusinessLogicNonRetryableException;
import org.fsociety.identityservice.exception.BusinessLogicRetryableException;

public interface BaseBusinessLogic<ResourceType> {

    /**
     * @param input is instance of {@link ResourceType}.
     * @return An instance of {@link ResourceType} which hold extra information then input which is id.
     * @throws BusinessLogicRetryableException which is checked exception and caller need to catch and understand it.
     */
    ResourceType addResource(final ResourceType input) throws BusinessLogicRetryableException;

    /**
     * @param resourceId is UUID 128-bit number to uniquely identify company from database.
     * @return {@link ResourceType} resource which fetched from database by using resourceId.
     * @throws BusinessLogicNonRetryableException which is checked exception and caller need to catch and understand it.
     */
    ResourceType getResource(final String resourceId) throws BusinessLogicNonRetryableException;

    /**
     * @param resourceId is UUID 128-bit number to uniquely identify company from database.
     * @param input is instance of {@link ResourceType}.
     * @return An instance of {@link ResourceType} with updated information.
     * @throws BusinessLogicNonRetryableException which is checked exception and caller need to catch and understand it.
     */
    ResourceType updateResource(final String resourceId, final ResourceType input) throws BusinessLogicNonRetryableException;

    /**
     * @param resourceId is UUID 128-bit number to uniquely identify company from database.
     * @throws BusinessLogicRetryableException which is checked retryable exception when database failed to delete resource.
     */
    void deleteResource(final String resourceId) throws BusinessLogicRetryableException;
}
