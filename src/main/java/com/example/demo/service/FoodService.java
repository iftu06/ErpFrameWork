package com.example.demo.service;

import com.example.demo.model.Food;
import com.example.demo.repository.FoodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Divineit-Iftekher on 8/12/2017.
 */
@Service
public class FoodService extends BaseService{


    @Autowired
    FoodService(FoodRepository  foodRepository){
        super(foodRepository,"Food");
    }

}
