package org.fsociety.identityservice.dao.impl;

import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.UpdateResult;
import lombok.extern.slf4j.Slf4j;
import org.bson.conversions.Bson;
import org.fsociety.identityservice.dao.AbstractMongoBaseDAO;
import org.fsociety.identityservice.dao.BaseCRUDDAO;
import org.fsociety.identityservice.exception.DAONonRetryableException;
import org.fsociety.identityservice.exception.DAORetryableException;
import org.springframework.util.StringUtils;

import java.util.Objects;
import java.util.UUID;

@Slf4j
public abstract class AbstractBaseCRUDDAOImpl<ResourceType> extends AbstractMongoBaseDAO
    implements BaseCRUDDAO<ResourceType> {

    /**
     * {@inheritDoc}
     */
    public ResourceType add(final ResourceType input) throws DAORetryableException {
        log.info("AbstractBaseCRUDDAOImpl's add called with input : {}", input);

        final ResourceType resource = input;
        setResourceId(resource, createResourceId());
        try {
            log.info("Inserting resource : {} in mongodb", resource);
            getResourceCollection().insertOne(resource);
            log.info("Successfully inserted resource : {} in mongodb", resource);
        } catch (final MongoException exception) {
            log.error("Failed to insert resource : {} in mongodb, exception : ", resource, exception);
            throw new DAORetryableException("Please re-try mongo having issue to insert document.", exception);
        }

        return resource;
    }

    private String createResourceId() {
        final String uuidInStringForm = UUID.randomUUID().toString();

        return uuidInStringForm.replaceAll("-","");
    }

    /**
     * Client need to implement this behaviour in concrete/sub class by calling required resource.setResourceId()
     * for example setResourceId for company can be setCompanyId.
     * @param resource Object which we need to change/update.
     * @param resourceId id which will be updated.
     */
    protected abstract void setResourceId(final ResourceType resource, final String resourceId);

    /**
     * {@inheritDoc}
     */
    public ResourceType get(final String id) {
        log.info("AbstractBaseCRUDDAOImpl's get called with id : {}", id);

        final Bson query = Filters.eq(getResourceIdFieldIdentifier(), id);
        final ResourceType resource = (ResourceType) getResourceCollection().find(query).first();
        log.info("For id : {} fetched resource is : {}", id, resource);

        return resource;
    }

    /**
     * {@inheritDoc}
     */
    public ResourceType update(final String id, final ResourceType resource) throws DAONonRetryableException {
        log.info("AbstractDAO's update called with id : {}, and resource : {}", id, resource);

        validateResourceForUpdateRequest(id, resource);

        final Bson filterQuery = Filters.eq(getResourceIdFieldIdentifier(), id);
        final ResourceType returnedResource = (ResourceType) getResourceCollection().findOneAndReplace(filterQuery,
                resource);

        if (Objects.nonNull(returnedResource)) {
            return resource;
        } else {
            throw new DAONonRetryableException(String.format("Resource not updated for given resourceId", id));
        }
    }

    private void validateResourceForUpdateRequest(final String id, final ResourceType resource)
            throws DAONonRetryableException {
        if (StringUtils.isEmpty(getResourceId(resource))) {
            log.warn("Resource: {} id was null thus setting request's path id: {}", resource, id);
            setResourceId(resource, id);
        } else if (getResourceId(resource).compareTo(id) != 0) {
            log.error("Error occurred because request's path id : {} and resource's id : {} is not equal", id,
                    getResourceId(resource));
            throw new DAONonRetryableException("Id in request's path and resource's id are not equal");
        }
    }

    /**
     * Client need to implement this behaviour in concrete/sub class by calling required resource.getResourceId()
     * for example company resource will be handled by getCompanyId().
     * @param resource Object from which we need to extract id.
     * @return fetched id from resource object.
     */
    protected abstract String getResourceId(final ResourceType resource);

    /**
     * {@inheritDoc}
     */
     public void delete(final String id) throws DAORetryableException {
         log.info("AbstractDAO's delete called with id : {}", id);

         try {
             final Bson query = Filters.eq(getResourceIdFieldIdentifier(), id);
             getResourceCollection().deleteOne(query);
             log.info("Successfully deleted resource where id is : {}", id);
         } catch (final MongoException exception) {
             log.error("Error occurred when trying to delete resource where id : {}, exception", id, exception);
             throw new DAORetryableException("Mongo server failed to delete resource for given id", exception);
         }
     }

    /**
     * This behaviour always return ResourceId field identifier. For example company -> companyId.
     * @return constant value defined by above statement.
     */
    protected abstract String getResourceIdFieldIdentifier();

    /**
     * In this beahviour client can use {@link AbstractMongoBaseDAO}'s getMongoCollection by passing required values.
     * @return mongoCollection for specific resource.
     */
    protected abstract MongoCollection getResourceCollection();
}
