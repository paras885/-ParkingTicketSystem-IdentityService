package businesslogic;

import com.pts.common.entities.Operator;
import common.AbstractBaseTest;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Tested;
import mockit.integration.junit4.JMockit;
import org.fsociety.identityservice.businesslogic.impl.OperatorBusinessLogicImpl;
import org.fsociety.identityservice.dao.OperatorDAO;
import org.fsociety.identityservice.exception.BusinessLogicNonRetryableException;
import org.fsociety.identityservice.exception.BusinessLogicRetryableException;
import org.fsociety.identityservice.exception.DAONonRetryableException;
import org.fsociety.identityservice.exception.DAORetryableException;
import org.hamcrest.core.Is;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JMockit.class)
public class OperatorBusinessLogicImplTest extends AbstractBaseTest {

    @Tested
    private OperatorBusinessLogicImpl operatorBusinessLogic;

    @Injectable
    private OperatorDAO operatorDAO;

    @Test
    public void testAddOperator_happyCase() throws DAORetryableException, BusinessLogicRetryableException {
        final Operator expectedOperator = mockOperator(false);
        new Expectations() {
            {
                operatorDAO.addOperator(expectedOperator);
                result = expectedOperator;
                times = 1;
            }
        };

        final Operator actualOperator = operatorBusinessLogic.addOperator(expectedOperator);
        Assert.assertThat(actualOperator, Is.is(expectedOperator));
    }

    @Test(expected = BusinessLogicRetryableException.class)
    public void testAddOperator_whenOperatorDAOImplThrowDAORetryableException() throws DAORetryableException,
        BusinessLogicRetryableException {
            final Operator input = mockOperator(false);
            new Expectations() {
                {
                    operatorDAO.addOperator(input);
                    result = new DAORetryableException("DAO Failed.", new NullPointerException());
                    times = 1;
                }
            };

            operatorBusinessLogic.addOperator(input);
    }

    @Test
    public void testGetOperator_happyCase() throws BusinessLogicNonRetryableException {
        final Operator expectedOperator = mockOperator(false);
        new Expectations() {
            {
                operatorDAO.getOperator(OPERATOR_ID);
                result = expectedOperator;
                times = 1;
            }
        };

        final Operator actualOperator = operatorBusinessLogic.getOperator(OPERATOR_ID);
        Assert.assertThat(actualOperator, Is.is(expectedOperator));
    }

    @Test(expected = BusinessLogicNonRetryableException.class)
    public void testGetOperator_whenOperatorIsNotPresent() throws BusinessLogicNonRetryableException {
        new Expectations() {
            {
                operatorDAO.getOperator(OPERATOR_ID);
                result = null;
                times = 1;
            }
        };

        operatorBusinessLogic.getOperator(OPERATOR_ID);
    }

    @Test
    public void testUpdateOperator_happyCase() throws DAONonRetryableException, BusinessLogicNonRetryableException {
        final Operator expectedOperator = mockOperator(false);
        new Expectations() {
            {
                operatorDAO.updateOperator(OPERATOR_ID, expectedOperator);
                result = expectedOperator;
                times = 1;
            }
        };

        final Operator actualOperator = operatorBusinessLogic.updateOperator(OPERATOR_ID, expectedOperator);
        Assert.assertThat(actualOperator, Is.is(expectedOperator));
    }

    @Test(expected = BusinessLogicNonRetryableException.class)
    public void testUpdateOperator_whenOperatorDAOImplThrowDAONonRetryableException() throws DAONonRetryableException,
        BusinessLogicNonRetryableException {
            final Operator expectedOperator = mockOperator(false);
            new Expectations() {
                {
                    operatorDAO.updateOperator(OPERATOR_ID, expectedOperator);
                    result = new DAONonRetryableException("DAO failed.");
                    times = 1;
                }
            };

            operatorBusinessLogic.updateOperator(OPERATOR_ID, expectedOperator);
    }
}
