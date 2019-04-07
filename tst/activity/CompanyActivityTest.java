package activity;

import com.pts.common.entities.Company;
import common.AbstractBaseTest;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Tested;
import mockit.integration.junit4.JMockit;
import org.fsociety.identityservice.activity.CompanyActivity;
import org.fsociety.identityservice.businesslogic.CompanyBusinessLogic;
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
public class CompanyActivityTest extends AbstractBaseTest {

    @Tested
    private CompanyActivity companyActivity;

    @Injectable
    private CompanyBusinessLogic businessLogic;

    @Test
    public void testAddCompany_happyCase() throws BusinessLogicRetryableException {
        final Company expectedCompany = mockCompany();
        new Expectations() {
            {
                businessLogic.addResource(expectedCompany);
                result = expectedCompany;
                times = 1;
            }
        };

        final ResponseEntity<Company> response = companyActivity.addResource(expectedCompany);
        Assert.assertThat(response.getStatusCode(), Is.is(HttpStatus.OK));
        Assert.assertThat(response.getBody(), Is.is(expectedCompany));
    }

    @Test
    public void testAddCompany_whenBusinessLogicFailToProcess() throws BusinessLogicRetryableException {
        final Company expectedCompany = mockCompany();
        new Expectations() {
            {
                businessLogic.addResource(expectedCompany);
                result = new BusinessLogicRetryableException("BusinessLogic failed.", new Exception());
                times = 1;
            }
        };

        final ResponseEntity<Company> response = companyActivity.addResource(expectedCompany);
        Assert.assertThat(response.getStatusCode(), Is.is(HttpStatus.FAILED_DEPENDENCY));
        Assert.assertThat(response.getBody(), IsNull.nullValue());
    }

    @Test
    public void testGetCompany_happyCase() throws BusinessLogicNonRetryableException {
        final Company expectedCompany = mockCompany();
        new Expectations() {
            {
                businessLogic.getResource(COMPANY_ID);
                result = expectedCompany;
                times = 1;
            }
        };

        final ResponseEntity<Company> response = companyActivity.getResource(COMPANY_ID);
        Assert.assertThat(response.getStatusCode(), Is.is(HttpStatus.OK));
        Assert.assertThat(response.getBody(), Is.is(expectedCompany));
    }

    @Test
    public void testGetCompany_whenBusinessLogicFailToProcess() throws BusinessLogicNonRetryableException {
        new Expectations() {
            {
                businessLogic.getResource(COMPANY_ID);
                result = new BusinessLogicNonRetryableException("Failed to fetch", new NullPointerException());
                times = 1;
            }
        };

        final ResponseEntity<Company> response = companyActivity.getResource(COMPANY_ID);
        Assert.assertThat(response.getStatusCode(), Is.is(HttpStatus.NOT_FOUND));
        Assert.assertThat(response.getBody(), IsNull.nullValue());
    }

    @Test
    public void testUpdateCompany_happyCase() throws BusinessLogicNonRetryableException {
        final Company expectedCompany = mockCompany();
        new Expectations() {
            {
                businessLogic.updateResource(COMPANY_ID, expectedCompany);
                result = expectedCompany;
                times = 1;
            }
        };

        final ResponseEntity<Company> response = companyActivity.updateResource(COMPANY_ID, expectedCompany);
        Assert.assertThat(response.getStatusCode(), Is.is(HttpStatus.OK));
        Assert.assertThat(response.getBody(), Is.is(expectedCompany));
    }

    @Test
    public void testUpdateCompany_whenBusinessLogicFailToProcess() throws BusinessLogicNonRetryableException {
        final Company input = mockCompany();
        new Expectations() {
            {
                businessLogic.updateResource(COMPANY_ID, input);
                result = new BusinessLogicNonRetryableException("BusinessLogic failed.", new Exception());
                times = 1;
            }
        };

        final ResponseEntity<Company> response = companyActivity.updateResource(COMPANY_ID, input);
        Assert.assertThat(response.getStatusCode(), Is.is(HttpStatus.BAD_REQUEST));
        Assert.assertThat(response.getBody(), IsNull.nullValue());
    }

    @Test
    public void testDeleteCompany_happyCase() throws BusinessLogicRetryableException {
        new Expectations() {
            {
                businessLogic.deleteResource(COMPANY_ID);
                times = 1;
            }
        };

        final  ResponseEntity<String> response = companyActivity.deleteResource(COMPANY_ID);
        Assert.assertThat(response.getStatusCode(), Is.is(HttpStatus.OK));
        Assert.assertThat(response.getBody(), StringContains.containsString("Success"));
    }

    @Test
    public void testDeleteCompany_whenBusinessLogicFailToProcess() throws BusinessLogicRetryableException {
        new Expectations() {
            {
                businessLogic.deleteResource(COMPANY_ID);
                result = new BusinessLogicRetryableException("Businesslogic failed.", new Exception());
                times = 1;
            }
        };

        final ResponseEntity<String> response = companyActivity.deleteResource(COMPANY_ID);
        Assert.assertThat(response.getStatusCode(), Is.is(HttpStatus.FAILED_DEPENDENCY));
        Assert.assertThat(response.getBody(), StringContains.containsString("Error"));
    }
}
