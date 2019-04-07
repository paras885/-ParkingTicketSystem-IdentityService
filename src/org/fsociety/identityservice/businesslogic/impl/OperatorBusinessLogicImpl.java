package org.fsociety.identityservice.businesslogic.impl;

import com.pts.common.entities.Operator;
import org.fsociety.identityservice.businesslogic.OperatorBusinessLogic;
import org.fsociety.identityservice.dao.BaseCRUDDAO;
import org.fsociety.identityservice.dao.OperatorDAO;
import org.fsociety.identityservice.dao.impl.OperatorDAOImpl;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component(value = OperatorBusinessLogicImpl.BEAN_IDENTIFIER)
public class OperatorBusinessLogicImpl extends AbstractBaseCRUDBusinessLogic<Operator>
    implements OperatorBusinessLogic {

    public static final String BEAN_IDENTIFIER = "operatorBusinessLogic";

    @Resource(name = OperatorDAOImpl.BEAN_IDENTIFIER)
    private OperatorDAO operatorDAO;

    @Override
    protected BaseCRUDDAO<Operator> getResourceDAO() {
        return operatorDAO;
    }
}
