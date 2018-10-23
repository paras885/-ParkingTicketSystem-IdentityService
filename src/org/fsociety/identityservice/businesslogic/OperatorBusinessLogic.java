package org.fsociety.identityservice.businesslogic;

import com.pts.common.entities.Operator;
import org.fsociety.identityservice.exception.BusinessLogicNonRetryableException;
import org.fsociety.identityservice.exception.BusinessLogicRetryableException;

public interface OperatorBusinessLogic {

    /**
     * @param input is instance of {@link Operator} which hold information like: name companyId and other details.
     * @return An instance of {@link Operator} which hold extra information then input which is id.
     * @throws BusinessLogicRetryableException which is checked exception and caller need to catch and understand it.
     */
    Operator addOperator(final Operator input) throws BusinessLogicRetryableException;

    /**
     * @param operatorId is UUID 128-bit number to uniquely identify operator from database.
     * @return Operator resource which fetched from database by using operatorId.
     * @throws BusinessLogicNonRetryableException which is checked exception and caller need to catch and understand it.
     */
    Operator getOperator(final String operatorId) throws BusinessLogicNonRetryableException;

    /**
     * @param operatorId is UUID 128-bit number to uniquely identify operator from database.
     * @param input is instance of {@link Operator} which hold information like: name companyId and other details.
     * @return An instance of {@link Operator} with updated information.
     * @throws BusinessLogicNonRetryableException which is checked exception and caller need to catch and understand it.
     */
    Operator updateOperator(final String operatorId, final Operator input) throws BusinessLogicNonRetryableException;

    /**
     * @param operatorId is UUID 128-bit number to uniquely identify operator from database.
     * @throws BusinessLogicRetryableException which is checked retryable exception when database failed to delete resource.
     */
    void deleteOperator(final String operatorId) throws BusinessLogicRetryableException;
}
