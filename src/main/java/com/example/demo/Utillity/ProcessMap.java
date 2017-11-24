package com.example.demo.Utillity;

import com.example.demo.UtilValidate;
import com.example.demo.service.SearchNode;
import com.example.demo.service.SearchProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Divineit-Iftekher on 11/14/2017.
 */
public class ProcessMap {

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

    public static SearchNode prepareSearchMap(Map queryMap) {

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
            currentNode.getSearchProperties().add(searchProperty);

        }
        return rootNode;
    }

    public static SearchNode composeSearchTree(SearchNode rootNode, SearchProperty searchProperty, String searchArr[]) {

        SearchNode currentNode = rootNode;
        for (int i = 1; i < searchArr.length - 3; i++) {
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
}
