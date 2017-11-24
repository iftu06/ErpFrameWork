package com.example.demo.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Divineit-Iftekher on 9/28/2017.
 */
@Entity
public class Restaurant extends BaseModel {

    private String restName;

    private String description;

    @Embedded
    AccessTime accessTime = new AccessTime(new Date(), new Date());

    @Embedded
    private Location location;


    private List<Food> foods = new ArrayList();


    @Column(name = "rest_name")
    public String getRestName() {
        return restName;
    }

    public void setResName(String resName) {
        this.restName = restName;
    }


    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
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

    public void setRestName(String restName) {
        this.restName = restName;
    }

    @ManyToMany(cascade = CascadeType.MERGE)
    public List<Food> getFoods() {
        return this.foods;
    }

    public void setFoods(List<Food> foods) {
        this.foods = foods;
    }
}
