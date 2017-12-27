package com.example.demo.Validate;

import com.example.demo.Utillity.ErrorObj;
import com.example.demo.Utillity.ProcessMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;


@Component
public class CustomValidate {

    @Autowired
    ProcessMap processMap;

    public List<ErrorObj> customValidate(String modelName, Object obj,String validatorPackage) {

        List<String> validatorMapList = processMap.getValidatorList(modelName);
        List<ErrorObj> errors = new ArrayList();

        for(String validator : validatorMapList ){

            String fullClassName = validatorPackage + "." + validator;
            try {
                Class modelclass = Class.forName(fullClassName);
                Object object = modelclass.newInstance();
                Method method = object.getClass().getMethod("validate",obj.getClass());
                ErrorObj errorObj= (ErrorObj) method.invoke(obj);
                errors.add(errorObj);

            }catch(Exception exp){

            }

        }
        
        return errors;
    }
}
