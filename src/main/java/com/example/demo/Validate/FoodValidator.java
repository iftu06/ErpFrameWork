package com.example.demo.Validate;


import com.example.demo.Utillity.ErrorObj;
import com.example.demo.model.Food;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * Created by Divineit-Iftekher on 12/16/2017.
 */
public class FoodValidator{


    public ErrorObj validate(Food food){


        return null;
    }

    public ErrorObj validateRating(int rating){
        ErrorObj obj = null;
        if(rating > 10){

        }
        return null;

    }
}
