package dao;

import com.mongodb.MongoException;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.pts.common.entities.Operator;

import common.AbstractBaseTest;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Mocked;
import mockit.Tested;
import mockit.integration.junit4.JMockit;
import org.bson.conversions.Bson;
import org.fsociety.identityservice.dao.impl.OperatorDAOImpl;
import org.fsociety.identityservice.exception.DAONonRetryableException;
import org.fsociety.identityservice.exception.DAORetryableException;
import org.hamcrest.core.Is;
import org.hamcrest.core.IsNull;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JMockit.class)
public class OperatorDAOImplTest extends AbstractBaseTest {

    @Tested
    private OperatorDAOImpl operatorDAO;

    @Injectable
    private MongoDatabase mongoDatabase;

    @Mocked
    private MongoCollection mongoCollection;

    private final static String COLLECTION_NAME = "Operators";
    private final static Class COLLECTION_CLASS = Operator.class;
    private final static String OPERATOR_ID_FIELD_IDENTIFIER = "operatorId";

    @Test
    public void testAddOperator_happyCase() throws DAORetryableException {
        new Expectations() {
            {
                mongoDatabase.getCollection(COLLECTION_NAME, COLLECTION_CLASS);
                result = mongoCollection;
                times = 1;

                mongoCollection.insertOne(any);
                times = 1;
            }
        };

        final Operator expectedOperator = mockOperator(false);
        final Operator actualOperator = operatorDAO.addOperator(expectedOperator);

        // Asserting few fields and then assuming other fields will be saved similarly.
        Assert.assertThat(actualOperator.getName(), Is.is(expectedOperator.getName()));
        Assert.assertThat(actualOperator.getCompanyId(), Is.is(expectedOperator.getCompanyId()));
        Assert.assertThat(actualOperator.getPassword(), Is.is(expectedOperator.getPassword()));
    }

    @Test(expected = DAORetryableException.class)
    public void testAddOperator_whenMongoFailToInsertDocument() throws DAORetryableException {
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

        operatorDAO.addOperator(mockOperator(false));
    }

    @Test
    public void testGetOperator_happyCase(@Mocked final FindIterable iterable) {
        final Operator expectedOperator = mockOperator(false);
        new Expectations() {
            {
                mongoDatabase.getCollection(COLLECTION_NAME, COLLECTION_CLASS);
                result = mongoCollection;
                times = 1;

                mongoCollection.find(withInstanceOf(Bson.class));
                result = iterable;
                times = 1;

                iterable.first();
                result = expectedOperator;
                times = 1;
            }
        };

        final Operator actualOperator = operatorDAO.getOperator(OPERATOR_ID);

        Assert.assertThat(actualOperator, Is.is(expectedOperator));
    }

    @Test
    public void testGetOperator_whenOperatorNotPresent(@Mocked final FindIterable iterable) {
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

        Assert.assertThat(operatorDAO.getOperator(OPERATOR_ID), IsNull.nullValue());
    }

    @Test
    public void testUpdateOperator_happyCase() throws DAONonRetryableException {
        final Operator expectedOperator = mockOperator(false);
        new Expectations() {
            {
                mongoDatabase.getCollection(COLLECTION_NAME, COLLECTION_CLASS);
                result = mongoCollection;
                times = 1;

                mongoCollection.findOneAndReplace(withInstanceOf(Bson.class), expectedOperator);
                times = 1;
            }
        };

        Assert.assertThat(operatorDAO.updateOperator(OPERATOR_ID, expectedOperator), Is.is(expectedOperator));
    }

    @Test
    public void testUpdateOperator_whenOperatorIdIsNotPresentInOperator() throws DAONonRetryableException {
        final Operator expectedOperatorWithoutOperatorId = mockOperator(false);
        expectedOperatorWithoutOperatorId.setOperatorId(null);

        final Operator expectedOperatorWithOperatorId = mockOperator(false);
        new Expectations() {
            {
                mongoDatabase.getCollection(COLLECTION_NAME, COLLECTION_CLASS);
                result = mongoCollection;
                times = 1;

                mongoCollection.findOneAndReplace(withInstanceOf(Bson.class), expectedOperatorWithOperatorId);
                times = 1;
            }
        };

        Assert.assertThat(operatorDAO.updateOperator(OPERATOR_ID, expectedOperatorWithoutOperatorId),
            Is.is(expectedOperatorWithOperatorId));
    }

    @Test(expected = DAONonRetryableException.class)
    public void testUpdateOperator_whenOperatorIdFromRequestAndOperatorAreNotEqual() throws DAONonRetryableException {
        final Operator operator = mockOperator(false);
        operatorDAO.updateOperator("randomOperatorId", operator);
    }

    @Test
    public void testDeleteOperator_happyCase() throws DAORetryableException {
        new Expectations() {
            {
                mongoDatabase.getCollection(COLLECTION_NAME, COLLECTION_CLASS);
                result = mongoCollection;
                times = 1;

                mongoCollection.deleteOne(withInstanceOf(Bson.class));
                times = 1;
            }
        };

        operatorDAO.deleteOperator(OPERATOR_ID);
    }

    @Test(expected = DAORetryableException.class)
    public void testDeleteOperator_whenMongoFailToDeleteOperatorResource() throws DAORetryableException {
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

        operatorDAO.deleteOperator(OPERATOR_ID);
    }
}
