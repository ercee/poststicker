package com.erce.poststicker.model;

public class FormDto {
    private String name;
    private String street1;
    private String street2;
    private String zipCode;
    private String city;
    private boolean selected;
    
    public String getName() {
        return name;
    }
    
    public void setName(String aName) {
        name = aName;
    }
    
    public String getStreet1() {
        return street1;
    }
    
    public void setStreet1(String aStreet1) {
        street1 = aStreet1;
    }
    
    public String getStreet2() {
        return street2;
    }
    
    public void setStreet2(String aStreet2) {
        street2 = aStreet2;
    }
    
    public String getZipCode() {
        return zipCode;
    }
    
    public void setZipCode(String aZipCode) {
        zipCode = aZipCode;
    }
    
    public String getCity() {
        return city;
    }
    
    public void setCity(String aCity) {
        city = aCity;
    }
    
    public boolean isSelected() {
        return selected;
    }
    
    public void setSelected(boolean aSelected) {
        selected = aSelected;
    }
}
