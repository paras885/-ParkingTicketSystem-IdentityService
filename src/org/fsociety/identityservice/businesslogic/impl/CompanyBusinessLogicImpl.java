package org.fsociety.identityservice.businesslogic.impl;

import com.pts.common.entities.Company;
import org.fsociety.identityservice.businesslogic.CompanyBusinessLogic;
import org.fsociety.identityservice.dao.BaseCRUDDAO;
import org.fsociety.identityservice.dao.CompanyDAO;
import org.fsociety.identityservice.dao.impl.CompanyDAOImpl;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component(value = CompanyBusinessLogicImpl.BEAN_IDENTIFIER)
public class CompanyBusinessLogicImpl extends AbstractBaseCRUDBusinessLogic<Company> implements CompanyBusinessLogic {

    public static final String BEAN_IDENTIFIER = "companyBusinessLogicImpl";

    @Resource(name = CompanyDAOImpl.BEAN_IDENTIFIER)
    private CompanyDAO companyDAO;

    @Override
    protected BaseCRUDDAO<Company> getResourceDAO() {
        return companyDAO;
    }
}
