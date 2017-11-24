package com.example.demo.service;

/**
 * Created by Divineit-Iftekher on 11/20/2017.
 */
public class SearchProperty {

    private String fieldName;
    private String opName;
    private Object value;

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getOpName() {
        return opName;
    }

    public void setOpName(String opName) {
        this.opName = opName;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
