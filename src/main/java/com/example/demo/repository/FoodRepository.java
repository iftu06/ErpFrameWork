package com.example.demo.repository;

import com.example.demo.model.Food;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Divineit-Iftekher on 8/9/2017.
 */
@Repository
public interface FoodRepository extends CrudRepository<Food,Integer> {

    @Override
    List<Food> findAll();
}
