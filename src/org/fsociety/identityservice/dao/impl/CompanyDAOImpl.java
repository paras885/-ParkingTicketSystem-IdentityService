package org.fsociety.identityservice.dao.impl;

import com.mongodb.client.MongoCollection;
import com.pts.common.entities.Company;
import lombok.extern.slf4j.Slf4j;
import org.fsociety.identityservice.dao.CompanyDAO;
import org.springframework.stereotype.Component;

@Slf4j
@Component(value = CompanyDAOImpl.BEAN_IDENTIFIER)
public class CompanyDAOImpl extends AbstractBaseCRUDDAOImpl<Company> implements CompanyDAO {

    public final static String BEAN_IDENTIFIER = "CompanyDAOImpl";

    private final static String COMPANY_ID_FIELD_IDENTIFIER = "companyId";

    @Override
    protected MongoCollection getResourceCollection() {
        return getCompanyCollection();
    }

    @Override
    protected String getResourceId(final Company company) {
        return company.getCompanyId();
    }

    @Override
    protected String getResourceIdFieldIdentifier() {
        return COMPANY_ID_FIELD_IDENTIFIER;
    }

    @Override
    protected void setResourceId(final Company company, final String companyId) {
        company.setCompanyId(companyId);
    }
}
