package com.example.demo.service;

import com.example.demo.repository.FoodRepository;
import com.example.demo.repository.RestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Divineit-Iftekher on 9/28/2017.
 */
@Service
public class RestaurantService extends BaseService{

    @Autowired
    RestaurantService(RestRepository restRepository){
        super(restRepository,"Restaurant");
    }
}
