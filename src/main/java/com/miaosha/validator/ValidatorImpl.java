package com.miaosha.validator;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Component
public class ValidatorImpl implements InitializingBean {

    private Validator validator;

    public ValidationResult validate(Object bean){
        ValidationResult validationResult=new ValidationResult();
        Set<ConstraintViolation<Object>> constraintValidatorSet=validator.validate(bean);
        if(constraintValidatorSet.size()>0){
            validationResult.setHasErrors(true);
            Map<String,String> result=new HashMap<String,String>();
            constraintValidatorSet.forEach((constraintViolation)->{
                String errMsg=constraintViolation.getMessage();
                String propertyName= constraintViolation.getPropertyPath().toString();
                result.put(propertyName,errMsg);
                validationResult.setErrorMsgMap(result);
                    }
            );


        }
        return validationResult;
    }



    @Override
    public void afterPropertiesSet() throws Exception {
        this.validator= Validation.buildDefaultValidatorFactory().getValidator();
    }
}
