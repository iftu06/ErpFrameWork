package com.example.demo.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Divineit-Iftekher on 8/8/2017.
 */
@Entity
public class Food extends BaseModel{


    private Integer rating;
    private String description;
    private String foodName;

    private List<Restaurant> restaurants = new ArrayList<Restaurant>();

    @Embedded
    AccessTime accessTime = new AccessTime(new Date(),new Date());


    @Column(name = "rating")
    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    @Column(name = "description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public AccessTime getAccessTime() {
        return accessTime;
    }

    public void setAccessTime(AccessTime accessTime) {
        this.accessTime = accessTime;
    }

    @Column(name = "food_name")
    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "Restaurant_Food", joinColumns = {@JoinColumn(name = "food_id")},
            inverseJoinColumns = {@JoinColumn(name = "restaurant_id")})
    public List<Restaurant> getRestaurants() {
        return restaurants;
    }

    public void setRestaurants(List<Restaurant> restaurants) {
        this.restaurants = restaurants;
    }
}
