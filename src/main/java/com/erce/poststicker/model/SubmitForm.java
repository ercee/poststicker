package com.erce.poststicker.model;

import java.util.*;

public class SubmitForm {
    private List<FormDto> formDtoList;
    private boolean onlyAdresses;
    
    public SubmitForm() {
        formDtoList = new ArrayList<>();
    }
    
    public List<FormDto> getFormDtoList() {
        return formDtoList;
    }
    
    public void setFormDtoList(List<FormDto> formDtoList) {
        this.formDtoList = formDtoList;
    }
    
    public boolean isOnlyAdresses() {
        return onlyAdresses;
    }
    
    public void setOnlyAdresses(boolean aOnlyAdresses) {
        onlyAdresses = aOnlyAdresses;
    }
}

