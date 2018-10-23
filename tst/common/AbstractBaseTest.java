package common;

import com.pts.common.entities.Operator;

public class AbstractBaseTest {

    protected final static String OPERATOR_ID = "operatorId";
    protected final static String OPERATOR_NAME = "name";
    protected final static String OPERATOR_PASSWORD = "password";
    protected final static String OPERATOR_ADDRESS = "address";
    protected final static String COMPANY_ID = "companyId";
    protected final static String OPERATOR_CONTACT_NUMBER = "contactNumber";
    protected final static String OPERATOR_EMAIL_ID = "paras.meena885@gmail.com";

    protected Operator mockOperator(final boolean isActive) {
        return Operator.builder()
            .operatorId(OPERATOR_ID)
            .name(OPERATOR_NAME)
            .password(OPERATOR_PASSWORD)
            .address(OPERATOR_ADDRESS)
            .companyId(COMPANY_ID)
            .contactNumber(OPERATOR_CONTACT_NUMBER)
            .emailId(OPERATOR_EMAIL_ID)
            .isActive(isActive)
            .build();
    }
}
