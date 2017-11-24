package com.example.demo.controller;

import com.example.demo.Utillity.ProcessMap;
import com.example.demo.service.FoodService;
import com.example.demo.model.Food;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.naming.Binding;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * Created by Divineit-Iftekher on 8/8/2017.
 */
@RestController
public class FoodController {

    @Autowired
    FoodService foodService;

    @RequestMapping("/food")
    public String foodView() {
        return "foodSave";
    }

    @CrossOrigin
    @RequestMapping(value = "/foods", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public void saveFood(@RequestBody String payload, HttpServletResponse resp, BindingResult res) throws JsonProcessingException, ClassNotFoundException {

        List<Map> reqMap = foodService.validateModel(payload);
        foodService.save(reqMap);
    }

    @CrossOrigin
    @RequestMapping(value = "/foods", method = RequestMethod.GET)
    public ResponseEntity<List<Food>> getFoods(@RequestParam String params, HttpServletResponse resp) throws Exception {

        Map queryMap = ProcessMap.convertStringToMap(params);
        ProcessMap.prepareSearchMap(queryMap);
        List<Food> foods = foodService.get();
        if (foods.isEmpty()) {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<List<Food>>(foods, HttpStatus.OK);
    }
}