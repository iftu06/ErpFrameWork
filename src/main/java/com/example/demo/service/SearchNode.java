package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Divineit-Iftekher on 11/20/2017.
 */
public class SearchNode {

    String nodeName;
    List<SearchNode> childNodes = new ArrayList<>();
    List<SearchProperty> searchProperties = new ArrayList();

    public SearchNode(String nodeName) {
        this.nodeName = nodeName;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public List<SearchNode> getChildNodes() {
        return childNodes;
    }

    public void setChildNodes(List<SearchNode> childNodes) {
        this.childNodes = childNodes;
    }

    public List<SearchProperty> getSearchProperties() {
        return searchProperties;
    }

    public void setSearchProperties(List<SearchProperty> searchProperties) {
        this.searchProperties = searchProperties;
    }
}
