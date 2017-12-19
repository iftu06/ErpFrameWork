package com.example.demo.service;

import com.example.demo.UtilValidate;
import com.example.demo.Utillity.ErrorObj;
import com.example.demo.Validate.CustomValidate;
import com.example.demo.model.Food;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.validation.ObjectError;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.lang.reflect.Field;
import java.util.*;

/**
 * Created by Divineit-Iftekher on 9/28/2017.
 */
public class BaseService {

    @Autowired
    CustomValidate customValidate;

    CrudRepository repository;
    private String modelName;

    private final String modelPackageName = "com.example.demo.model";
    private final String validatorPackage = "com.example.demo.Validate";

    Session session;
    @PersistenceContext
    EntityManager em;

    public void criteriaTest(Class modelClass) {

        Criteria cr = session.createCriteria(modelClass);
    }

    BaseService() {

    }

    BaseService(CrudRepository repository, String modelName) {
        this.repository = repository;
        this.modelName = modelName;
    }

    public void saveBean(List<Object> objects) {
        repository.save(objects);

    }

    public void save(List<Map> reqMapList) throws ClassNotFoundException {
        BeanWrapper bean = null;
        ObjectMapper mapper = new ObjectMapper();
        List<Object> listOfModel = new ArrayList<>();
        Class modelclass = this.getModelClass();
        try {
            for (Map reqMap : reqMapList) {
                Map modelMap = (Map) reqMap.get(this.modelName);
                bean = preparemodel(modelclass, modelMap);

                customValidate.customValidate(this.getValidatorClassName(),bean.getWrappedInstance());

                listOfModel.add(bean.getWrappedInstance());
                this.saveBean(listOfModel);
            }
        } catch (Exception ex) {

            ex.printStackTrace();
        }
    }

    private static Field getField(Class clazz, String fieldName)
            throws NoSuchFieldException {
        try {
            return clazz.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            Class superClass = clazz.getSuperclass();
            if (superClass == null) {
                throw e;
            } else {
                return getField(superClass, fieldName);
            }
        }
    }


    public BeanWrapper preparemodel(Class modelclass, Map modelMap) throws ClassNotFoundException, IllegalAccessException, InstantiationException, NoSuchFieldException {
        Iterator it = modelMap.entrySet().iterator();
        BeanWrapper bean = new BeanWrapperImpl(modelclass.newInstance());
        List<Field> fields = new ArrayList<>(Arrays.asList(modelclass.getDeclaredFields()));
        try {
            Field idField = getField(modelclass, "id");
            idField.setAccessible(true);
            fields.add(idField);
        } catch (NoSuchFieldException exp) {
            exp.printStackTrace();
        }
        for (Field field : fields) {
            String fieldName = field.getName();
            if (!UtilValidate.isEmpty(modelMap.get(field.getName()))) {
                String type = modelMap.get(fieldName).getClass().getSimpleName();
                if (type.equals("LinkedHashMap")) {
                    BeanWrapper innerBean = null;
                    String fullClassName = this.modelPackageName + "." + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1, fieldName.length());
                    Class mClass = Class.forName(fullClassName);
                    innerBean = preparemodel(mClass, (Map) modelMap.get(field.getName()));
                    bean.setPropertyValue(fieldName, innerBean.getWrappedInstance());
                } else if (type.equals("ArrayList")) {
                    List<Map> innerList = (List) modelMap.get(field.getName());
                    List<Object> innerObject = new ArrayList<>();
                    for (Map innerMap : innerList) {
                        BeanWrapper innerBean = null;
                        String fullClassName = this.modelPackageName + "." + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1, fieldName.length() - 1);
                        Class mClass = Class.forName(fullClassName);
                        innerBean = preparemodel(mClass, innerMap);
                        innerObject.add(innerBean.getWrappedInstance());
                    }
                    bean.setPropertyValue(fieldName, innerObject);
                } else {
                    bean.setPropertyValue(field.getName(), modelMap.get(field.getName()));
                }
            }

        }
        return bean;
    }

    public List validateModel(String requestData) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        List<Map> reqMapList = new ArrayList<>();
        try {
            reqMapList = mapper.readValue(requestData, List.class);
            for (Map reqMap : reqMapList) {
                Map modelMap = (Map) reqMap.get(this.modelName);
                validateModel(modelMap);
            }
        } catch (Exception ex) {

        }
        return reqMapList;
    }

    public void validateModel(Map modelMap) throws ClassNotFoundException, NoSuchFieldException {
        Object obj = null;
        List errors = new ArrayList();
        Class modelClass = null;
        try {
            modelClass = this.getModelClass();

            Iterator modelIt = modelMap.entrySet().iterator();
            while (modelIt.hasNext()) {
                Map.Entry entry = (Map.Entry) modelIt.next();
                String fieldName = (String) entry.getKey();
                String fieldVal = (String) entry.getValue();
                Field f = modelClass.getDeclaredField(fieldName);
                //field cast validation
                obj = castFieldOrRaiseTypeCastError(f.getType().getSimpleName(), fieldVal);
                if (obj instanceof ErrorObj) {
                    errors.add(obj);
                } else {
                    modelMap.put(fieldName, obj);
                }
            }
            if (errors.size() > 0) {
                modelMap.put("errors", errors);
            }
        } catch (ClassNotFoundException exp) {
            throw exp;
        } catch (NoSuchFieldException exp) {
            throw exp;
        }

    }

    public Object customValidate(String fieldType, String fieldVal) throws NoSuchFieldException, ClassNotFoundException {

        return castFieldOrRaiseTypeCastError(fieldType, fieldVal);

    }

    public Object castFieldOrRaiseTypeCastError(String fieldType, String fieldVal) throws ClassNotFoundException, NoSuchFieldException {

        ErrorObj error = null;
        Object val = null;
        try {
            if (fieldType.equals("Integer")) {
                val = Integer.parseInt(fieldVal);
            } else if (fieldType.equals("Float")) {
                val = Float.parseFloat(fieldVal);
            } else if (fieldType.equals("Double")) {
                val = Double.parseDouble(fieldVal);
            } else if (fieldType.equals("String")) {
                val = fieldVal;
            }
        } catch (ClassCastException exp) {
            error = new ErrorObj(fieldType, "Class Cast");
        } catch (NumberFormatException ex) {
            error = new ErrorObj(fieldType, "Value should be number");
        } finally {
            if (error != null)
                return error;
        }

        return val;


    }

    public Class getModelClass() throws ClassNotFoundException {
        Class modelClass = null;
        try {
            modelClass = Class.forName(this.getFullclassName());
        } catch (ClassNotFoundException exp) {
            throw exp;
        }
        return modelClass;
    }

    public List get() {
        return (List) repository.findAll();

    }

    public String getFullclassName() {
        return modelPackageName + "." + this.modelName;
    }

    public String getModelName() {
        return this.modelName;
    }

    public String getValidatorClassName() {
        return this.validatorPackage+this.modelName;
    }
}
