package com.example.demo.service;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Tuple;
import javax.persistence.criteria.*;
import java.util.*;

/**
 * Created by Divineit-Iftekher on 11/25/2017.
 */
@Component
public class QueryBuilderImpl implements QueryBuilder {

    @PersistenceContext
    private EntityManager em;

    CriteriaBuilder cb;

    Criteria cr;

    Session session;

    CriteriaQuery qt;

    @Autowired
    ProcessMap processMap ;

    List<Predicate> predicates = new ArrayList<>();
    List<Selection<?>> selections = new ArrayList<>();

    public QueryBuilderImpl() {


    }

    public Criteria getCriteria(Class modelClass) {
        return em.unwrap(Session.class).createCriteria(modelClass);
    }

    public CriteriaQuery getCriteriaQuery() {
        return cb.createTupleQuery();
    }

    public List list(SearchNode searchNode, Class modelClass, String modelName) {
        cb = em.getCriteriaBuilder();
        qt = cb.createQuery(Tuple.class);
        Root root = qt.from(modelClass);
        this.prepareSelectionList(modelName, root);
        this.preparePredicateList(searchNode, root, null, null);
        qt.multiselect(selections);
        qt.where(cb.and(predicates.toArray(new Predicate[0])));

        List<Tuple> resultList = em.createQuery(qt).getResultList();


        return resultList;
    }

    public void preparePredicateList(SearchNode searchNode, Root root, Path path, Join uJoin) {
        if (path == null) {
            path = root;
        }

        int flag = 0;

        List<SearchProperty> searchProperties = searchNode.getSearchProperties();
        for (SearchProperty searchProperty : searchProperties) {
            Class<? extends Comparable> clazzz = (Class<? extends Comparable>) searchProperty.getValue().getClass();
            if (searchProperty.getOpName().equals("eq")) {
                predicates.add(this.cb.equal(path.get(searchProperty.getFieldName()), searchProperty.getValue()));
            } else if (searchProperty.getOpName().equals("gt")) {
                predicates.add(this.cb.greaterThanOrEqualTo(path.get(searchProperty.getFieldName()), clazzz.cast(searchProperty.getValue())));
            } else if (searchProperty.getOpName().equals("lt")) {
                predicates.add(this.cb.lessThanOrEqualTo(path.get(searchProperty.getFieldName()), clazzz.cast(searchProperty.getValue())));
            } else if (searchProperty.getOpName().equals("%st")) {
                predicates.add(this.cb.like(path.get(searchProperty.getFieldName()), (String) searchProperty.getValue() + "%"));
            } else if (searchProperty.getOpName().equals("%en")) {
                predicates.add(this.cb.like(path.get(searchProperty.getFieldName()), "%" + (String) searchProperty.getValue()));
            } else if (searchProperty.getOpName().equals("%cn")) {
                predicates.add(this.cb.like(path.get(searchProperty.getFieldName()), "%" + (String) searchProperty.getValue() + "%"));
            }

            if (!selections.contains(path.get(searchProperty.getFieldName()))) {
                selections.add(path.get(searchProperty.getFieldName()));
            }

        }


        List<SearchNode> childs = searchNode.getChildNodes();

        if (flag == 0) {
            for (SearchNode child : childs) {
                Join cJoin = root.join(child.getNodeName());
                child.setJoin(cJoin);
            }
        }

        flag++;

        for (SearchNode child : childs) {
            Join sJoin = null;
            if (child.getJoin() == null) {
                sJoin = uJoin.join(child.getNodeName());
                child.setJoin(sJoin);
            } else {
                sJoin = child.getJoin();
            }
            preparePredicateList(child, root, sJoin, sJoin);
        }


    }

    public void prepareSelectionList(String fileName, Root root) {

        List<Map> schemaList = processMap.getSchemaMapFromFile(fileName);
        for (Map schemaMap : schemaList) {
            String fieldName = (String) schemaMap.get("fieldName");
            Path path = root.get(fieldName);
            if (selections.contains(path)) {
                selections.add(path);
            }

        }

    }


}
