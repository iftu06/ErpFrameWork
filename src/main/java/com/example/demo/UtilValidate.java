package com.example.demo;

/**
 * Created by Divineit-Iftekher on 10/24/2017.
 */
public class UtilValidate {

    public static boolean isEmpty(Object obj) {
        if (obj == null) {
            return true;
        }
        if (obj instanceof java.lang.String) {
            return obj.toString().equals("") ? true : false;
        }
        return false;
    }
}
