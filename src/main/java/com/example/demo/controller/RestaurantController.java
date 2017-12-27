package com.example.demo.controller;

import com.example.demo.model.Food;
import com.example.demo.model.Restaurant;
import com.example.demo.service.FoodService;
import com.example.demo.service.RestaurantService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * Created by Divineit-Iftekher on 8/8/2017.
 */
@RestController
public class RestaurantController {

    @Autowired
    RestaurantService restaurantService;


    @CrossOrigin
    @RequestMapping(value = "/restaurants", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public void saveFood(@RequestBody String payload, HttpServletResponse resp, BindingResult res) throws JsonProcessingException, ClassNotFoundException {

        List reqMap = restaurantService.validateTypeCast(payload);
        List listOfBean = restaurantService.validateBean(reqMap);

        restaurantService.saveBean(listOfBean);
    }

    @CrossOrigin
    @RequestMapping(value = "/restaurants", method = RequestMethod.GET)
    public ResponseEntity<List<Restaurant>> getFoods(HttpServletResponse resp) {
        List<Restaurant> restaurants = restaurantService.get();
        if (restaurants.isEmpty()) {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<List<Restaurant>>(restaurants, HttpStatus.OK);
    }
}
