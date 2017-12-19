package com.example.demo.Validate;

import com.example.demo.Utillity.ErrorObj;
import com.example.demo.service.ProcessMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by Divineit-Iftekher on 12/18/2017.
 */
@Component
public class CustomValidate {

    @Autowired
    ProcessMap processMap;

    public ErrorObj customValidate(String modelName, Object obj) {

        List validatorMap = processMap.getValidatorMapFromFile(modelName);
        
        return null;
    }
}
