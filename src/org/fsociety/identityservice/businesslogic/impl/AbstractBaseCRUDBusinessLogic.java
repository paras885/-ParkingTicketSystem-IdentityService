package org.fsociety.identityservice.businesslogic.impl;

import org.fsociety.identityservice.businesslogic.BaseBusinessLogic;
import org.fsociety.identityservice.dao.BaseCRUDDAO;
import org.fsociety.identityservice.exception.BusinessLogicNonRetryableException;
import org.fsociety.identityservice.exception.BusinessLogicRetryableException;
import org.fsociety.identityservice.exception.DAONonRetryableException;
import org.fsociety.identityservice.exception.DAORetryableException;

import java.util.Objects;

public abstract class AbstractBaseCRUDBusinessLogic<ResourceType> implements BaseBusinessLogic<ResourceType> {

    /**
     * {@inheritDoc}
     */
    @Override
    public ResourceType addResource(final ResourceType input) throws BusinessLogicRetryableException {
        try {
            return getResourceDAO().add(input);
        } catch (final DAORetryableException exception) {
            throw new BusinessLogicRetryableException("Exception is retryable, mongo server facing some issue",
                exception);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResourceType getResource(final String resourceId) throws BusinessLogicNonRetryableException {
        final ResourceType resource = getResourceDAO().get(resourceId);
        if (Objects.isNull(resource)) {
            final Exception nullPointerException = new NullPointerException("Resource not found");
            throw new BusinessLogicNonRetryableException("For given resourceId there was no record",
                nullPointerException);
        } else {
            return resource;
        }
    }

    protected abstract BaseCRUDDAO<ResourceType> getResourceDAO();

    /**
     * {@inheritDoc}
     */
    @Override
    public ResourceType updateResource(final String resourceId, final ResourceType resource)
        throws BusinessLogicNonRetryableException {
        try {
            return getResourceDAO().update(resourceId, resource);
        } catch (final DAONonRetryableException exception) {
            throw new BusinessLogicNonRetryableException("Non retryable error please verify input", exception);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteResource(final String resourceId) throws BusinessLogicRetryableException {
        try {
            getResourceDAO().delete(resourceId);
        } catch (final DAORetryableException exception) {
            throw new BusinessLogicRetryableException("Database failed to delete resource", exception);
        }
    }
}
