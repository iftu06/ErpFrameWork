package com.example.demo.service;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * Created by Divineit-Iftekher on 11/28/2017.
 */
public interface QueryBuilder {

    public void prepareSelectionList(String fileName, Root root);
    public void preparePredicateList(SearchNode searchNode, Root root, Path path,Join join);
    public List list(SearchNode searchNode, Class modelClass, String modelName);
}
