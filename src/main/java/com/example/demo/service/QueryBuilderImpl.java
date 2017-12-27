package com.example.demo.service;

import com.example.demo.UtilValidate;
import com.example.demo.Utillity.ProcessMap;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    ProcessMap processMap;

    List<Predicate> predicates = new ArrayList<>();
    List<Selection<?>> selections = new ArrayList<>();
    List<String> fields = new ArrayList<>();
    private String aliasConcatenate = "";

    public QueryBuilderImpl() {


    }

    public Criteria getCriteria(Class modelClass) {
        return em.unwrap(Session.class).createCriteria(modelClass);
    }

    public CriteriaQuery getCriteriaQuery() {
        return cb.createTupleQuery();
    }

    public List list(SearchNode searchNode, Class modelClass, String modelName) throws JsonProcessingException {
        cb = em.getCriteriaBuilder();
        qt = cb.createQuery(Tuple.class);
        Root root = qt.from(modelClass);
        this.prepareSelectionList(modelName, root);
        qt.multiselect(selections);
        if (!UtilValidate.isEmpty(searchNode)) {
            this.preparePredicateList(searchNode, root, null, null);
            qt.where(cb.and(predicates.toArray(new Predicate[0])));
        }

        List<Tuple> tuples = em.createQuery(qt).getResultList();
        List<Map<String,Object>> result = new ArrayList();


        if(!tuples.isEmpty()){
            result = prepareResultMap(tuples);
        }
        this.selections.clear();
        this.predicates.clear();
        this.fields.clear();

        return result;
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
                String aliasName = searchProperty.getAliasName();
                selections.add(path.get(searchProperty.getFieldName()).alias(aliasName));
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
            fields.add(fieldName);
            Path path = root.get(fieldName);
            if (!selections.contains(path)) {
                selections.add(path.alias(fieldName));
            }

        }

    }

    public List<Map<String,Object>> prepareResultMap (List<Tuple> resultList){

        List<Map<String,Object>> results= new ArrayList<Map<String,Object>>();
        Map<String,Object> rowMap = new HashMap<String,Object>();
        for(Tuple tuple:resultList){
            for(String field : fields){
                Object tupleVal = tuple.get(field);
                rowMap.put(field,tupleVal);
            }
            results.add(rowMap);
            rowMap = new HashMap<String,Object>();

        }

        return results;
    }

//    public Map prepareNestedMap(String field,Object val){
//        String arr[] = field.split("_");
//        Map map = new HashMap();
//        for(String a : arr){
//
//        }
//    }


}
