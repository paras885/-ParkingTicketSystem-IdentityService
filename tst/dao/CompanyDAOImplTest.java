package dao;

import com.mongodb.MongoException;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.pts.common.entities.Company;
import common.AbstractBaseTest;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Mocked;
import mockit.Tested;
import mockit.integration.junit4.JMockit;
import org.bson.conversions.Bson;
import org.fsociety.identityservice.dao.impl.CompanyDAOImpl;
import org.fsociety.identityservice.exception.DAONonRetryableException;
import org.fsociety.identityservice.exception.DAORetryableException;
import org.hamcrest.core.Is;
import org.hamcrest.core.IsNull;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JMockit.class)
public class CompanyDAOImplTest extends AbstractBaseTest {

    @Tested
    private CompanyDAOImpl companyDAO;

    @Injectable
    private MongoDatabase mongoDatabase;

    @Mocked
    private MongoCollection mongoCollection;

    private final static String COLLECTION_NAME = "Companies";
    private final static Class COLLECTION_CLASS = Company.class;
    private final static String COMPANY_ID_FIELD_IDENTIFIER = "companyId";

    @Test
    public void testAddCompany_happyCase() throws DAORetryableException {
        new Expectations() {
            {
                mongoDatabase.getCollection(COLLECTION_NAME, COLLECTION_CLASS);
                result = mongoCollection;
                times = 1;

                mongoCollection.insertOne(any);
                times = 1;
            }
        };

        final Company expectedCompany = mockCompany();
        final Company actualCompany = companyDAO.add(expectedCompany);

        // Asserting few fields and then assuming other fields will be saved similarly.
        Assert.assertThat(actualCompany.getName(), Is.is(expectedCompany.getName()));
        Assert.assertThat(actualCompany.getCompanyId(), Is.is(expectedCompany.getCompanyId()));
    }

    @Test(expected = DAORetryableException.class)
    public void testAddCompany_whenMongoFailToInsertDocument() throws DAORetryableException {
        new Expectations() {
            {
                mongoDatabase.getCollection(COLLECTION_NAME, COLLECTION_CLASS);
                result = mongoCollection;
                times = 1;

                mongoCollection.insertOne(any);
                result = new MongoException("failed to insert document");
                times = 1;
            }
        };

        companyDAO.add(mockCompany());
    }

    @Test
    public void testGetCompany_happyCase(@Mocked final FindIterable iterable) {
        final Company expectedCompany = mockCompany();
        new Expectations() {
            {
                mongoDatabase.getCollection(COLLECTION_NAME, COLLECTION_CLASS);
                result = mongoCollection;
                times = 1;

                mongoCollection.find(withInstanceOf(Bson.class));
                result = iterable;
                times = 1;

                iterable.first();
                result = expectedCompany;
                times = 1;
            }
        };

        final Company actualCompany = companyDAO.get(COMPANY_ID);

        Assert.assertThat(actualCompany, Is.is(expectedCompany));
    }

    @Test
    public void testGetCompany_whenCompanyNotPresent(@Mocked final FindIterable iterable) {
        new Expectations() {
            {
                mongoDatabase.getCollection(COLLECTION_NAME, COLLECTION_CLASS);
                result = mongoCollection;
                times = 1;

                mongoCollection.find(withInstanceOf(Bson.class));
                result = iterable;
                times = 1;

                iterable.first();
                result = null;
                times = 1;
            }
        };

        Assert.assertThat(companyDAO.get(COMPANY_ID), IsNull.nullValue());
    }

    @Test
    public void testUpdateCompany_happyCase() throws DAONonRetryableException {
        final Company expectedCompany = mockCompany();
        new Expectations() {
            {
                mongoDatabase.getCollection(COLLECTION_NAME, COLLECTION_CLASS);
                result = mongoCollection;
                times = 1;

                mongoCollection.findOneAndReplace(withInstanceOf(Bson.class), expectedCompany);
                result = expectedCompany;
                times = 1;
            }
        };

        Assert.assertThat(companyDAO.update(COMPANY_ID, expectedCompany), Is.is(expectedCompany));
    }

    @Test
    public void testUpdateCompany_whenCompanyIdIsNotPresentInCompany() throws DAONonRetryableException {
        final Company expectedCompanyWithoutCompanyId = mockCompany();
        expectedCompanyWithoutCompanyId.setCompanyId(null);

        final Company expectedCompanyWithCompanyId = mockCompany();
        new Expectations() {
            {
                mongoDatabase.getCollection(COLLECTION_NAME, COLLECTION_CLASS);
                result = mongoCollection;
                times = 1;

                mongoCollection.findOneAndReplace(withInstanceOf(Bson.class), expectedCompanyWithCompanyId);
                result = expectedCompanyWithCompanyId;
                times = 1;
            }
        };

        Assert.assertThat(companyDAO.update(COMPANY_ID, expectedCompanyWithoutCompanyId),
            Is.is(expectedCompanyWithCompanyId));
    }

    @Test(expected = DAONonRetryableException.class)
    public void testUpdateCompany_whenCompanyIdFromRequestAndCompanyAreNotEqual() throws DAONonRetryableException {
        final Company company = mockCompany();
        companyDAO.update("randomCompanyId", company);
    }

    @Test
    public void testDeleteCompany_happyCase() throws DAORetryableException {
        new Expectations() {
            {
                mongoDatabase.getCollection(COLLECTION_NAME, COLLECTION_CLASS);
                result = mongoCollection;
                times = 1;

                mongoCollection.deleteOne(withInstanceOf(Bson.class));
                times = 1;
            }
        };

        companyDAO.delete(COMPANY_ID);
    }

    @Test(expected = DAORetryableException.class)
    public void testDeleteCompany_whenMongoFailToDeleteCompanyResource() throws DAORetryableException {
        new Expectations() {
            {
                mongoDatabase.getCollection(COLLECTION_NAME, COLLECTION_CLASS);
                result = mongoCollection;
                times = 1;

                mongoCollection.deleteOne(withInstanceOf(Bson.class));
                result = new MongoException("Failed to delete.");
                times = 1;
            }
        };

        companyDAO.delete(COMPANY_ID);
    }
}
