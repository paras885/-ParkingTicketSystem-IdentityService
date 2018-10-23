package org.fsociety.identityservice.dao;

import com.pts.common.entities.Operator;
import org.fsociety.identityservice.exception.DAONonRetryableException;
import org.fsociety.identityservice.exception.DAORetryableException;

public interface OperatorDAO {

    /**
     * @param input is an instance of {@link Operator} which hold operator related details.
     * @return Added operator details.
     * @throws DAORetryableException If database failed to add resource then this exception will be thrown.
     */
    Operator addOperator(final Operator input) throws DAORetryableException;

    /**
     * @param operatorId for which we need to fetch operator resource.
     * @return instance of {@link Operator}.
     */
    Operator getOperator(final String operatorId);

    /**
     * @param operatorId for which we need to update operator resource.
     * @param operator instance of {@link Operator} what values need to be updated.
     * @return instance of {@link Operator} with updated values.
     * @throws DAONonRetryableException If provided information is not valid then it will be thrown.
     */
    Operator updateOperator(final String operatorId, final Operator operator) throws DAONonRetryableException;

    /**
     * @param operatorId for which we need to delete operator resource.
     * @throws DAORetryableException If database failed to delete resource then this exception will be thrown.
     */
    void deleteOperator(final String operatorId) throws DAORetryableException;
}
