package org.fsociety.identityservice.activity;


import com.pts.common.entities.Company;
import lombok.extern.slf4j.Slf4j;
import org.fsociety.identityservice.businesslogic.BaseBusinessLogic;
import org.fsociety.identityservice.businesslogic.CompanyBusinessLogic;
import org.fsociety.identityservice.businesslogic.impl.CompanyBusinessLogicImpl;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Slf4j
@RequestMapping(value = CompanyActivity.COMPANY_RESOURCE_URL)
@RestController
public class CompanyActivity extends AbstractBaseCRUDActivity<Company> {

    public static final String COMPANY_RESOURCE_URL = "/companies";

    @Resource(name = CompanyBusinessLogicImpl.BEAN_IDENTIFIER)
    private CompanyBusinessLogic companyBusinessLogic;

    @Override
    protected BaseBusinessLogic<Company> getBusinessLogic() {
        return companyBusinessLogic;
    }
}
