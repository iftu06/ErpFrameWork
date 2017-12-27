package com.example.demo.controller;

import com.example.demo.Utillity.ProcessMap;
import com.example.demo.service.*;
import com.example.demo.model.Food;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.persistence.Tuple;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * Created by Divineit-Iftekher on 8/8/2017.
 */
@RestController
public class FoodController {

    @Autowired
    FoodService foodService;

    @Autowired
    ProcessMap processMap;

    @Autowired
    QueryBuilder qb;

    @RequestMapping("/food")
    public String foodView() {
        return "foodSave";
    }

    @CrossOrigin
    @RequestMapping(value = "/foods", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public Object saveFood(@RequestBody String payload, HttpServletResponse resp, BindingResult res) throws Exception {
        List<Map> reqMap = foodService.validateTypeCast(payload);
        if (foodService.isModelContainError()) {
            return reqMap;
        }
        List listOfBean = foodService.validateBean(reqMap);

        if (foodService.isModelContainError()) {
            return listOfBean;
        } else {
            foodService.saveBean(listOfBean);
            return this.getFoods("", resp);
        }
    }

    @CrossOrigin
    @RequestMapping(value = "/foods", method = RequestMethod.GET)
    public Object getFoods(@RequestParam String params, HttpServletResponse resp) throws Exception {

        SearchNode searchNode = null;
        Map queryMap = ProcessMap.convertStringToMap(params);
        if (!queryMap.isEmpty()) {
            searchNode = ProcessMap.prepareSearchMap(queryMap);
        }
        List<Map> list = qb.list(searchNode, foodService.getModelClass(), foodService.getModelName());
        if (list.isEmpty()) {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
        return list;
    }


}
