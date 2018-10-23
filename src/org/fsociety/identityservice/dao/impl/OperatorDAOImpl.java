package org.fsociety.identityservice.dao.impl;

import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.pts.common.entities.Operator;
import lombok.extern.slf4j.Slf4j;
import org.bson.conversions.Bson;
import org.fsociety.identityservice.dao.AbstractMongoBaseDAO;
import org.fsociety.identityservice.dao.OperatorDAO;
import org.fsociety.identityservice.exception.DAONonRetryableException;
import org.fsociety.identityservice.exception.DAORetryableException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.UUID;

@Slf4j
@Component(OperatorDAOImpl.BEAN_NAME)
public class OperatorDAOImpl extends AbstractMongoBaseDAO implements OperatorDAO {

    public final static String BEAN_NAME = "OperatorDAOImpl";

    private final static String OPERATOR_COLLECTION_NAME = "Operators";
    private final static String OPERATOR_ID_FIELD_IDENTIFIER = "operatorId";

    /**
     * {@inheritDoc}
     */
    @Override
    public Operator addOperator(final Operator input) throws DAORetryableException {
        log.info("OperatorDAOImpl's addOperator called with input : {}", input);

        final Operator operator = input;
        operator.setOperatorId(createOperatorId());
        try {
            log.info("Inserting operator : {} in mongodb", operator);
            getOperatorCollection().insertOne(operator);
            log.info("Successfully inserted operator : {} in mongodb", operator);
        } catch (final MongoException exception) {
            log.error("Failed to insert operator : {} in mongodb, exception : ", operator, exception);
            throw new DAORetryableException("Please re-try mongo having issue to insert document.", exception);
        }

        return operator;
    }

    private MongoCollection getOperatorCollection() {
        return getMongoCollection(OPERATOR_COLLECTION_NAME, Operator.class);
    }

    private String createOperatorId() {
        final String uuidInStringForm = UUID.randomUUID().toString();

        return uuidInStringForm.replaceAll("-","");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Operator getOperator(final String operatorId) {
        log.info("OperatorDAOImpl's getOperator called with operatorId : {}", operatorId);


        final Bson query = Filters.eq(OPERATOR_ID_FIELD_IDENTIFIER, operatorId);
        final Operator operator = (Operator) getOperatorCollection().find(query).first();
        log.info("For operatorId : {} fetched operator is : {}", operatorId, operator);

        return operator;
    }

    /*
     * There can be a case when someone miss to put operatorId in request because anyway he/she sending this information
     * in request itself so to avoid these type of edge cases we update operator pojo on our side.
     */
    /**
     * {@inheritDoc}
     */
    @Override
    public Operator updateOperator(final String operatorId, final Operator operator) throws DAONonRetryableException {
        log.info("OperatorDAOImpl's updateOperator called with operatorId : {}, and operator : {}", operatorId,
            operator);

        if (StringUtils.isEmpty(operator.getOperatorId())) {
            log.warn("Resource's operator's : {} operatorId was null thus setting request's path operatorId: {}",
                operator, operatorId);
            operator.setOperatorId(operatorId);
        } else if (operator.getOperatorId().compareTo(operatorId) != 0) {
            log.error(
                "Error occurred because request's path operatorId : {} and operator resource's id : {} is not equal",
                operatorId, operator.getOperatorId());
            throw new DAONonRetryableException("Operator id in request's path and operator resource are not equal");
        }

        final Bson query = Filters.eq(OPERATOR_ID_FIELD_IDENTIFIER, operatorId);
        getOperatorCollection().findOneAndReplace(query, operator);

        return operator;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteOperator(final String operatorId) throws DAORetryableException {
        log.info("OperatorDAOImpl's deleteOperator called with operatorId : {}", operatorId);

        try {
            final Bson query = Filters.eq(OPERATOR_ID_FIELD_IDENTIFIER, operatorId);
            getOperatorCollection().deleteOne(query);
            log.info("Successfully deleted operator resource where operatorId is : {}", operatorId);
        } catch (final MongoException exception) {
            log.error("Error occurred when trying to delete operator where operatorId : {}, exception", operatorId,
                exception);
            throw new DAORetryableException("Mongo server failed to delete operator for given operatorId", exception);
        }
    }
}
