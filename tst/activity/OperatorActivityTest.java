package activity;

import com.pts.common.entities.Operator;
import common.AbstractBaseTest;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Tested;
import mockit.integration.junit4.JMockit;
import org.fsociety.identityservice.activity.OperatorActivity;
import org.fsociety.identityservice.businesslogic.OperatorBusinessLogic;
import org.fsociety.identityservice.exception.BusinessLogicNonRetryableException;
import org.fsociety.identityservice.exception.BusinessLogicRetryableException;
import org.hamcrest.core.Is;
import org.hamcrest.core.IsNull;
import org.hamcrest.core.StringContains;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@RunWith(JMockit.class)
public class OperatorActivityTest extends AbstractBaseTest {

    @Tested
    private OperatorActivity operatorActivity;

    @Injectable
    private OperatorBusinessLogic businessLogic;

    @Test
    public void testAddOperator_happyCase() throws BusinessLogicRetryableException {
        final Operator expectedOperator = mockOperator(false);
        new Expectations() {
            {
                businessLogic.addResource(expectedOperator);
                result = expectedOperator;
                times = 1;
            }
        };

        final ResponseEntity<Operator> response = operatorActivity.addResource(expectedOperator);
        Assert.assertThat(response.getStatusCode(), Is.is(HttpStatus.OK));
        Assert.assertThat(response.getBody(), Is.is(expectedOperator));
    }

    @Test
    public void testAddOperator_whenBusinessLogicFailToProcess() throws BusinessLogicRetryableException {
        final Operator expectedOperator = mockOperator(false);
        new Expectations() {
            {
                businessLogic.addResource(expectedOperator);
                result = new BusinessLogicRetryableException("BusinessLogic failed.", new Exception());
                times = 1;
            }
        };

        final ResponseEntity<Operator> response = operatorActivity.addResource(expectedOperator);
        Assert.assertThat(response.getStatusCode(), Is.is(HttpStatus.FAILED_DEPENDENCY));
        Assert.assertThat(response.getBody(), IsNull.nullValue());
    }

    @Test
    public void testGetOperator_happyCase() throws BusinessLogicNonRetryableException {
        final Operator expectedOperator = mockOperator(false);
        new Expectations() {
            {
                businessLogic.getResource(OPERATOR_ID);
                result = expectedOperator;
                times = 1;
            }
        };

        final ResponseEntity<Operator> response = operatorActivity.getResource(OPERATOR_ID);
        Assert.assertThat(response.getStatusCode(), Is.is(HttpStatus.OK));
        Assert.assertThat(response.getBody(), Is.is(expectedOperator));
    }

    @Test
    public void testGetOperator_whenBusinessLogicFailToProcess() throws BusinessLogicNonRetryableException {
        new Expectations() {
            {
                businessLogic.getResource(OPERATOR_ID);
                result = new BusinessLogicNonRetryableException("Failed to fetch", new NullPointerException());
                times = 1;
            }
        };

        final ResponseEntity<Operator> response = operatorActivity.getResource(OPERATOR_ID);
        Assert.assertThat(response.getStatusCode(), Is.is(HttpStatus.NOT_FOUND));
        Assert.assertThat(response.getBody(), IsNull.nullValue());
    }

    @Test
    public void testUpdateOperator_happyCase() throws BusinessLogicNonRetryableException {
        final Operator expectedOperator = mockOperator(false);
        new Expectations() {
            {
                businessLogic.updateResource(OPERATOR_ID, expectedOperator);
                result = expectedOperator;
                times = 1;
            }
        };

        final ResponseEntity<Operator> response = operatorActivity.updateResource(OPERATOR_ID, expectedOperator);
        Assert.assertThat(response.getStatusCode(), Is.is(HttpStatus.OK));
        Assert.assertThat(response.getBody(), Is.is(expectedOperator));
    }

    @Test
    public void testUpdateOperator_whenBusinessLogicFailToProcess() throws BusinessLogicNonRetryableException {
        final Operator input = mockOperator(false);
        new Expectations() {
            {
                businessLogic.updateResource(OPERATOR_ID, input);
                result = new BusinessLogicNonRetryableException("BusinessLogic failed.", new Exception());
                times = 1;
            }
        };

        final ResponseEntity<Operator> response = operatorActivity.updateResource(OPERATOR_ID, input);
        Assert.assertThat(response.getStatusCode(), Is.is(HttpStatus.BAD_REQUEST));
        Assert.assertThat(response.getBody(), IsNull.nullValue());
    }

    @Test
    public void testDeleteOperator_happyCase() throws BusinessLogicRetryableException {
        new Expectations() {
            {
                businessLogic.deleteResource(OPERATOR_ID);
                times = 1;
            }
        };

        final  ResponseEntity<String> response = operatorActivity.deleteResource(OPERATOR_ID);
        Assert.assertThat(response.getStatusCode(), Is.is(HttpStatus.OK));
        Assert.assertThat(response.getBody(), StringContains.containsString("Success"));
    }

    @Test
    public void testDeleteOperator_whenBusinessLogicFailToProcess() throws BusinessLogicRetryableException {
        new Expectations() {
            {
                businessLogic.deleteResource(OPERATOR_ID);
                result = new BusinessLogicRetryableException("Businesslogic failed.", new Exception());
                times = 1;
            }
        };

        final ResponseEntity<String> response = operatorActivity.deleteResource(OPERATOR_ID);
        Assert.assertThat(response.getStatusCode(), Is.is(HttpStatus.FAILED_DEPENDENCY));
        Assert.assertThat(response.getBody(), StringContains.containsString("Error"));
    }
}
