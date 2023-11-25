package com.erce.poststicker.model;

import java.util.*;

public class SubmitForm {
    private List<FormDto> formDtoList;
    
    public SubmitForm() {
        formDtoList = new ArrayList<>();
    }
    
    public List<FormDto> getFormDtoList() {
        return formDtoList;
    }
    
    public void setFormDtoList(List<FormDto> formDtoList) {
        this.formDtoList = formDtoList;
    }
}

