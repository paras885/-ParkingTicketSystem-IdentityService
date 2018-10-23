package org.fsociety.identityservice.activity;

import com.pts.common.entities.Operator;
import lombok.extern.slf4j.Slf4j;
import org.fsociety.identityservice.businesslogic.OperatorBusinessLogic;
import org.fsociety.identityservice.businesslogic.impl.OperatorBusinessLogicImpl;
import org.fsociety.identityservice.exception.BusinessLogicNonRetryableException;
import org.fsociety.identityservice.exception.BusinessLogicRetryableException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Slf4j
@RestController
public class OperatorActivity {

    private static final String OPERATOR_RESOURCE_URL = "/operators";

    @Resource(name = OperatorBusinessLogicImpl.BEAN_IDENTIFIER)
    private OperatorBusinessLogic operatorBusinessLogic;

    @RequestMapping(value = OPERATOR_RESOURCE_URL, method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Operator> addOperator(final @RequestBody Operator operator) {
        log.info("Request to add operator is : {}", operator);

        ResponseEntity<Operator> response = null;
        try {
            response = new ResponseEntity<Operator>(operatorBusinessLogic.addOperator(operator), HttpStatus.OK);
            log.info("Operator : {} added successfully.", operator);
        } catch (final BusinessLogicRetryableException exception) {
            log.error("Error occurred when trying to add operator : {}, exception", operator, exception);
            response = new ResponseEntity<>(null, HttpStatus.FAILED_DEPENDENCY);
        }

        return response;
    }

    @RequestMapping(value = OPERATOR_RESOURCE_URL + "/{operatorId}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Operator> getOperator(final @PathVariable String operatorId) {
        log.info("Request to fetch operator where opertorId is : {}", operatorId);

        ResponseEntity<Operator> response = null;
        try {
            final Operator operator = operatorBusinessLogic.getOperator(operatorId);
            log.info("For operatorId : {}, fetched Operator is : {}", operatorId, operator);
            response = new ResponseEntity<Operator>(operator, HttpStatus.OK);
        } catch (final BusinessLogicNonRetryableException exception) {
            log.error("Error occurred when trying to fetch operator for operatorId : {}, exception", operatorId,
                exception);
            response = new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        return response;
    }

    @RequestMapping(value = OPERATOR_RESOURCE_URL + "/{operatorId}", method = RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity<Operator> updateOperator(final @PathVariable String operatorId,
                                                   final @RequestBody Operator operator) {
        log.info("Request to put operator resource where operatorId is : {} and operator : {}", operatorId, operator);

        ResponseEntity<Operator> response = null;
        try {
            response = new ResponseEntity<Operator>(operatorBusinessLogic.updateOperator(operatorId, operator),
                HttpStatus.OK);
            log.info("For operatorId: {} operator updated with these values : {}", operatorId, operator);
        } catch (final BusinessLogicNonRetryableException exception) {
            final String logMessage =
                "Error occurred when tried to put new value where operatorId : {} and operator : {}, exception";
            log.error(String.format(logMessage, operatorId, operator), exception);
            response = new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

        return response;
    }

    @RequestMapping(value = OPERATOR_RESOURCE_URL + "/{operatorId}", method = RequestMethod.DELETE)
    @ResponseBody
    public ResponseEntity<String> deleteOperator(final @PathVariable String operatorId) {
        log.info("Request to delete operator resource where operatorId is : {}", operatorId);

        ResponseEntity<String> response = null;
        try {
            operatorBusinessLogic.deleteOperator(operatorId);
            response = new ResponseEntity<String>("Successfully deleted operator resource", HttpStatus.OK);
        } catch (final BusinessLogicRetryableException exception) {
            final String errorMessage =
                String.format("Error occurred when tried to delete operator resource where operatorId : %s",
                    operatorId);
            log.error(errorMessage, exception);
            response = new ResponseEntity<String>(errorMessage, HttpStatus.FAILED_DEPENDENCY);
        }

        return response;
    }
}
