package org.fsociety.identityservice.businesslogic.impl;

import com.pts.common.entities.Operator;
import org.fsociety.identityservice.businesslogic.OperatorBusinessLogic;
import org.fsociety.identityservice.dao.OperatorDAO;
import org.fsociety.identityservice.dao.impl.OperatorDAOImpl;
import org.fsociety.identityservice.exception.BusinessLogicNonRetryableException;
import org.fsociety.identityservice.exception.BusinessLogicRetryableException;
import org.fsociety.identityservice.exception.DAONonRetryableException;
import org.fsociety.identityservice.exception.DAORetryableException;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Objects;

@Component(value = OperatorBusinessLogicImpl.BEAN_IDENTIFIER)
public class OperatorBusinessLogicImpl implements OperatorBusinessLogic {

    public static final String BEAN_IDENTIFIER = "operatorBusinessLogic";

    @Resource(name = OperatorDAOImpl.BEAN_NAME)
    private OperatorDAO operatorDAO;

    /**
     * {@inheritDoc}
     */
    @Override
    public Operator addOperator(final Operator input) throws BusinessLogicRetryableException {
        try {
            return operatorDAO.addOperator(input);
        } catch (final DAORetryableException exception) {
            throw new BusinessLogicRetryableException("Exception is retryable, mongo server facing some issue",
                exception);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Operator getOperator(final String operatorId) throws BusinessLogicNonRetryableException {
        final Operator operator = operatorDAO.getOperator(operatorId);
        if (Objects.isNull(operator)) {
            final Exception nullPointerException = new NullPointerException("Operator not found");
            throw new BusinessLogicNonRetryableException("For given operatorId there was no record",
                nullPointerException);
        } else {
            return operator;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Operator updateOperator(final String operatorId, final Operator operator)
        throws BusinessLogicNonRetryableException {
            try {
                return operatorDAO.updateOperator(operatorId, operator);
            } catch (final DAONonRetryableException exception) {
                throw new BusinessLogicNonRetryableException("Non retryable error please verify passed input",
                    exception);
            }
    }

    @Override
    public void deleteOperator(final String operatorId) throws BusinessLogicRetryableException {
        try {
            operatorDAO.deleteOperator(operatorId);
        } catch (final DAORetryableException exception) {
            throw new BusinessLogicRetryableException("Database failed to delete resource", exception);
        }
    }
}
