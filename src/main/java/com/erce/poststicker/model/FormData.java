package com.erce.poststicker.model;

import java.util.*;

public class FormData {
    public static final String AL_AD = "AlAd";
    private Map<String, String> fieldMap;
    
    // Constructor
    public FormData() {
        fieldMap = new HashMap<>();
        initializeFieldMap();
    }
    
    // Initialize the field map with default values or leave it empty
    private void initializeFieldMap() {
        fieldMap.put("GondKod", "");
        fieldMap.put("GondSehir", "");
        fieldMap.put("GondAd", "");
        fieldMap.put("GondAdres1", "");
        fieldMap.put("GondAdres2", "");
        fieldMap.put(AL_AD, "");
        fieldMap.put("AlAdres1", "");
        fieldMap.put("AlAdres2", "");
        fieldMap.put("AlKod", "");
        fieldMap.put("AlSehir", "");
    }
    
    // Getters and setters for the field map
    public Map<String, String> getFieldMap() {
        return fieldMap;
    }
    
    public void setFieldMap(Map<String, String> fieldMap) {
        this.fieldMap = fieldMap;
    }
    
    // Method to set a specific field value
    public void setFieldValue(String fieldName, String value) {
        fieldMap.put(fieldName, value);
    }
    
    // Method to get a specific field value
    public String getFieldValue(String fieldName) {
        return fieldMap.get(fieldName);
    }
}
