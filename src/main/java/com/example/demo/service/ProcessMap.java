package com.example.demo.service;

import com.example.demo.UtilValidate;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Created by Divineit-Iftekher on 11/14/2017.
 */
@Component
public class ProcessMap {


    ResourceLoader resourceLoader;

    @Autowired
    public ProcessMap(ResourceLoader resourceLoader){
        this.resourceLoader = resourceLoader;
    }
    //this method is used to convert string to map
    public static Map convertStringToMap(String mapStr) throws Exception {

        ObjectMapper mapper = new ObjectMapper();
        Map resultMap = new HashMap();

        if (!UtilValidate.isEmpty(mapStr)) {
            resultMap = mapper.readValue(mapStr, HashMap.class);
            return resultMap;
        } else {
            throw new Exception("Empty String to Convert");
        }

    }

    public static SearchNode prepareSearchMap(Map queryMap) throws JsonProcessingException {

        Iterator it = queryMap.entrySet().iterator();
        SearchNode rootNode = null;
        SearchNode currentNode = null;
        Map rootMap = new HashMap();
        while (it.hasNext()) {
            Map.Entry<String, Object> entry = (Map.Entry) it.next();
            String searchKey = entry.getKey();
            Object searchVal = entry.getValue();
            String searchArr[] = searchKey.split("_");
            if (rootNode == null) {
                rootNode = new SearchNode(searchArr[0]);
            }
            String operatorName = searchArr[searchArr.length - 1];
            String fieldName = searchArr[searchArr.length - 2];
            SearchProperty searchProperty = new SearchProperty();
                searchProperty.setValue(searchVal);
                searchProperty.setFieldName(fieldName);
                searchProperty.setOpName(operatorName);
                currentNode = composeSearchTree(rootNode, searchProperty, searchArr);
                if(!UtilValidate.isEmpty(searchVal)) {
                    currentNode.getSearchProperties().add(searchProperty);
                }

        }
        ObjectMapper mapper = new ObjectMapper();
        System.out.println("Root Node " + mapper.writeValueAsString(rootNode));
        return rootNode;
    }

    public static SearchNode composeSearchTree(SearchNode rootNode, SearchProperty searchProperty, String searchArr[]) {

        SearchNode currentNode = rootNode;
        for (int i = 1; i < searchArr.length - 2; i++) {
            currentNode = containsNode(searchArr[i], currentNode);
        }

        return currentNode;
    }

    public static SearchNode containsNode(String searchNodeName, SearchNode rootNode) {
        SearchNode searchNode = new SearchNode(searchNodeName);
        List<SearchNode> childs = rootNode.getChildNodes();
        if (childs.isEmpty()) {
            childs.add(searchNode);
            return searchNode;
        }
        for (SearchNode node : childs) {

            if (rootNode.getNodeName().equals(node.getNodeName())) {
                return node;
            }
        }

        childs.add(searchNode);
        return searchNode;
    }


    public List<Map> getSchemaMapFromFile(String fileName)  {

        String fileNameWithExtension = ResourceLoader.CLASSPATH_URL_PREFIX+fileName + ".json";
        File jsonFile = null;
        ObjectMapper mapper = new ObjectMapper();
        List<Map> columnList = new ArrayList();
        Resource resource = resourceLoader.getResource(fileNameWithExtension);
        try {
            jsonFile = resource.getFile();
        } catch (IOException exp) {
            exp.printStackTrace();
        }
        try {
            columnList = mapper.readValue(jsonFile, List.class);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return columnList;
    }



    public List<Map> getValidatorMapFromFile(String modelName)  {

        String fileNameWithExtension = ResourceLoader.CLASSPATH_URL_PREFIX+"Validator/"+modelName + ".json";
        File jsonFile = null;
        ObjectMapper mapper = new ObjectMapper();
        Map validatorMap = new HashMap();
        List validatorList = new ArrayList();
        Resource resource = resourceLoader.getResource(fileNameWithExtension);
        try {
            jsonFile = resource.getFile();
        } catch (IOException exp) {
            exp.printStackTrace();
        }
        try {
            validatorMap = mapper.readValue(jsonFile, HashMap.class);
            validatorList = (List)validatorMap.get(modelName);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return validatorList;
    }




}
