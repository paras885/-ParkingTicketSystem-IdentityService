package businesslogic;

import com.pts.common.entities.Company;
import common.AbstractBaseTest;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Tested;
import mockit.integration.junit4.JMockit;
import org.fsociety.identityservice.businesslogic.impl.CompanyBusinessLogicImpl;
import org.fsociety.identityservice.dao.CompanyDAO;
import org.fsociety.identityservice.exception.BusinessLogicNonRetryableException;
import org.fsociety.identityservice.exception.BusinessLogicRetryableException;
import org.fsociety.identityservice.exception.DAONonRetryableException;
import org.fsociety.identityservice.exception.DAORetryableException;
import org.hamcrest.core.Is;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JMockit.class)
public class CompanyBusinessLogicImplTest extends AbstractBaseTest {

    @Tested
    private CompanyBusinessLogicImpl companyBusinessLogic;

    @Injectable
    private CompanyDAO companyDAO;

    @Test
    public void testAddCompany_happyCase() throws DAORetryableException, BusinessLogicRetryableException {
        final Company expectedCompany = mockCompany();
        new Expectations() {
            {
                companyDAO.add(expectedCompany);
                result = expectedCompany;
                times = 1;
            }
        };

        final Company actualCompany = companyBusinessLogic.addResource(expectedCompany);
        Assert.assertThat(actualCompany, Is.is(expectedCompany));
    }

    @Test(expected = BusinessLogicRetryableException.class)
    public void testAddCompany_whenCompanyDAOImplThrowDAORetryableException() throws DAORetryableException,
        BusinessLogicRetryableException {
        final Company input = mockCompany();
        new Expectations() {
            {
                companyDAO.add(input);
                result = new DAORetryableException("DAO Failed.", new NullPointerException());
                times = 1;
            }
        };

        companyBusinessLogic.addResource(input);
    }

    @Test
    public void testGetCompany_happyCase() throws BusinessLogicNonRetryableException {
        final Company expectedCompany = mockCompany();
        new Expectations() {
            {
                companyDAO.get(COMPANY_ID);
                result = expectedCompany;
                times = 1;
            }
        };

        final Company actualCompany = companyBusinessLogic.getResource(COMPANY_ID);
        Assert.assertThat(actualCompany, Is.is(expectedCompany));
    }

    @Test(expected = BusinessLogicNonRetryableException.class)
    public void testGetCompany_whenCompanyIsNotPresent() throws BusinessLogicNonRetryableException {
        new Expectations() {
            {
                companyDAO.get(COMPANY_ID);
                result = null;
                times = 1;
            }
        };

        companyBusinessLogic.getResource(COMPANY_ID);
    }

    @Test
    public void testUpdateCompany_happyCase() throws DAONonRetryableException, BusinessLogicNonRetryableException {
        final Company expectedCompany = mockCompany();
        new Expectations() {
            {
                companyDAO.update(COMPANY_ID, expectedCompany);
                result = expectedCompany;
                times = 1;
            }
        };

        final Company actualCompany = companyBusinessLogic.updateResource(COMPANY_ID, expectedCompany);
        Assert.assertThat(actualCompany, Is.is(expectedCompany));
    }

    @Test(expected = BusinessLogicNonRetryableException.class)
    public void testUpdateCompany_whenCompanyDAOImplThrowDAONonRetryableException() throws DAONonRetryableException,
        BusinessLogicNonRetryableException {
        final Company expectedCompany = mockCompany();
        new Expectations() {
            {
                companyDAO.update(COMPANY_ID, expectedCompany);
                result = new DAONonRetryableException("DAO failed.");
                times = 1;
            }
        };

        companyBusinessLogic.updateResource(COMPANY_ID, expectedCompany);
    }
}
