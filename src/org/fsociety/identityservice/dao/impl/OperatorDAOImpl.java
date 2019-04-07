package org.fsociety.identityservice.dao.impl;

import com.mongodb.client.MongoCollection;
import com.pts.common.entities.Operator;
import lombok.extern.slf4j.Slf4j;
import org.fsociety.identityservice.dao.OperatorDAO;
import org.springframework.stereotype.Component;

@Slf4j
@Component(OperatorDAOImpl.BEAN_IDENTIFIER)
public class OperatorDAOImpl extends AbstractBaseCRUDDAOImpl<Operator> implements OperatorDAO {

    public final static String BEAN_IDENTIFIER = "OperatorDAOImpl";

    private final static String OPERATOR_ID_FIELD_IDENTIFIER = "operatorId";

    @Override
    protected MongoCollection getResourceCollection() {
        return getOperatorCollection();
    }

    @Override
    protected String getResourceId(final Operator operator) {
        return operator.getOperatorId();
    }

    @Override
    protected String getResourceIdFieldIdentifier() {
        return OPERATOR_ID_FIELD_IDENTIFIER;
    }

    @Override
    protected void setResourceId(final Operator operator, final String operatorId) {
        operator.setOperatorId(operatorId);
    }
}
