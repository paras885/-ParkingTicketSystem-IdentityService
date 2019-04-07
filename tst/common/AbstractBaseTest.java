package common;

import com.pts.common.entities.Company;
import com.pts.common.entities.Operator;

import java.util.Arrays;
import java.util.List;

public class AbstractBaseTest {

    protected final static String OPERATOR_ID = "operatorId";
    protected final static String OPERATOR_NAME = "name";
    protected final static String OPERATOR_PASSWORD = "password";
    protected final static String OPERATOR_ADDRESS = "address";
    protected final static String OPERATOR_CONTACT_NUMBER = "contactNumber";
    protected final static String OPERATOR_EMAIL_ID = "paras.meena885@gmail.com";

    protected final static String BUILDING_ID = "buildingId";

    protected final static String COMPANY_ID = "companyId";
    protected final static String COMPANY_NAME = "companyName";
    protected final static String COMPANY_EMAIL_ID = "companyEmailId";
    protected final static String COMPANY_CONTACT_NUMBER = "companyContactNumber";
    protected final static String COMPANY_ADDRESS = "companyAddress";
    protected final static List<String> BUILDING_IDS = Arrays.asList(BUILDING_ID);

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

    protected Company mockCompany() {
        return Company.builder()
            .companyId(COMPANY_ID)
            .name(COMPANY_NAME)
            .address(COMPANY_ADDRESS)
            .emailId(COMPANY_EMAIL_ID)
            .contactNumber(COMPANY_CONTACT_NUMBER)
            .buildingIds(BUILDING_IDS)
            .build();
    }
}
