package org.fsociety.identityservice.dao;

import org.fsociety.identityservice.exception.DAONonRetryableException;
import org.fsociety.identityservice.exception.DAORetryableException;

public interface BaseCRUDDAO<ResourceType> {

    /**
     * @param input is an instance of {@link ResourceType}.
     * @return Added {@link ResourceType} details.
     * @throws DAORetryableException If database failed to add resource then this exception will be thrown.
     */
    ResourceType add(final ResourceType input) throws DAORetryableException;

    /**
     * @param id for which we need to fetch {@link ResourceType} resource.
     * @return instance of {@link ResourceType}.
     */
    ResourceType get(final String id);

    /**
     * @param id for which we need to update {@link ResourceType} resource.
     * @param resource instance of {@link ResourceType} what values need to be updated.
     * @return instance of {@link ResourceType} with updated values.
     * @throws DAONonRetryableException If provided information is not valid then it will be thrown.
     */
    ResourceType update(final String id, final ResourceType resource) throws DAONonRetryableException;

    /**
     * @param id for which we need to delete {@link ResourceType} resource.
     * @throws DAORetryableException If database failed to delete resource then this exception will be thrown.
     */
    void delete(final String id) throws DAORetryableException;
}
