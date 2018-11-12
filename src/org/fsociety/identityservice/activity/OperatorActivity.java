package org.fsociety.identityservice.activity;

import com.pts.common.entities.Operator;
import lombok.extern.slf4j.Slf4j;
import org.fsociety.identityservice.businesslogic.BaseBusinessLogic;
import org.fsociety.identityservice.businesslogic.OperatorBusinessLogic;
import org.fsociety.identityservice.businesslogic.impl.OperatorBusinessLogicImpl;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Slf4j
@RequestMapping(value = OperatorActivity.OPERATOR_RESOURCE_URL)
@RestController
public class OperatorActivity extends AbstractBaseCRUDActivity<Operator> {

    public static final String OPERATOR_RESOURCE_URL = "/operators";

    @Resource(name = OperatorBusinessLogicImpl.BEAN_IDENTIFIER)
    private OperatorBusinessLogic operatorBusinessLogic;

    @Override
    protected BaseBusinessLogic<Operator> getBusinessLogic() {
        return operatorBusinessLogic;
    }
}
