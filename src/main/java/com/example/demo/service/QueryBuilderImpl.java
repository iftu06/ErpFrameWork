package com.example.demo.service;

import org.hibernate.Criteria;
import org.hibernate.Session;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.Tuple;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Divineit-Iftekher on 11/25/2017.
 */
public class QueryBuilderImpl implements QueryBuilder{

    @PersistenceContext
    private EntityManager emt;

    CriteriaBuilder cb;

    Criteria cr;

    Session session;

    CriteriaQuery qt;

    List<Predicate> predicates = new ArrayList<>();
    List<Selection<?>> selections = new ArrayList<>();

    QueryBuilderImpl() {
        cb = emt.getCriteriaBuilder();

    }

    public Criteria getCriteria(Class modelClass) {
        return emt.unwrap(Session.class).createCriteria(modelClass);
    }

    public CriteriaQuery getCriteriaQuery() {
        return cb.createTupleQuery();
    }

    public Query getQuery(SearchNode searchNode, Class modelClass) {
        qt = (CriteriaQuery<Tuple>) this.getCriteria(Tuple.class);
        Root root = qt.from(modelClass);
        Path p = root.get("");
        qt.multiselect(selections);
        qt.where(cb.and(predicates.toArray(new Predicate[0])));


        return null;
    }

    public void preparePredicateList(SearchNode searchNode, Root root, Path path) {
        if (path == null) {
            path = root;
        }


        List<SearchProperty> searchProperties = searchNode.getSearchProperties();
        for (SearchProperty searchProperty : searchProperties) {
            Class<? extends Comparable> clazzz = (Class<? extends Comparable>) searchProperty.getValue().getClass();
            if (searchProperty.getOpName().equals("eq")) {
                predicates.add(this.cb.equal(path.get(searchProperty.getFieldName()), searchProperty.getValue()));
            } else if (searchProperty.getOpName().equals("gt")) {
                predicates.add(this.cb.greaterThanOrEqualTo(path.get(searchProperty.getFieldName()), clazzz.cast(searchProperty.getValue())));
            } else if (searchProperty.getOpName().equals("lt")) {
                predicates.add(this.cb.lessThanOrEqualTo(path.get(searchProperty.getFieldName()), clazzz.cast(searchProperty.getValue())));
            }

        }
        List<SearchNode> childs = searchNode.getChildNodes();
        for (SearchNode child : childs) {
            Join j = root.join(child.getNodeName());
            preparePredicateList(child, root,j);
        }


    }

    public void prepareSelectionList(SearchNode searchNode, Root root, Path path) {
        if (path == null) {
            path = root;
        }
        List<SearchProperty> searchPropertise = searchNode.getSearchProperties();
        for (SearchProperty searchProperty : searchPropertise) {
            selections.add(path.get(searchProperty.getFieldName()));
        }

        List<SearchNode> childs = searchNode.getChildNodes();
        for (SearchNode child : childs) {
            Path childPath = path.get(child.nodeName);
            preparePredicateList(child, root, childPath);
        }
    }


    public void prepareJoin(SearchNode searchNode, Root root, Join join) {


        List<SearchNode> childs = searchNode.getChildNodes();
        for (SearchNode child : childs) {
            if(join == null) {
                join = root.join(child.nodeName);
            }
            preparePredicateList(child, root, join);
        }
    }


}
