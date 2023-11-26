package com.erce.poststicker.service;

import java.util.*;
import org.springframework.stereotype.Service;
import com.erce.poststicker.model.*;
import org.apache.poi.util.StringUtil;

@Service
public class OrderFormMapper {
    
    public List<FormDto> map(List<Order> orders) {
        return orders.stream().filter(e -> e.getDateShipped() != "")
                .map(e -> {
                    FormDto formData = new FormDto();
                    formData.setName(e.getFullName());
                    formData.setStreet1(e.getStreet1());
                    LinkedList<String> addressDetails = new LinkedList<>();
                    if (!StringUtil.isBlank(e.getStreet2())) {
                        addressDetails.add(e.getStreet2());
                    }
                    if (!StringUtil.isBlank(e.getShipCity())) {
                        addressDetails.add(e.getShipCity());
                    }
                    if (!StringUtil.isBlank(e.getShipState())) {
                        addressDetails.add(e.getShipState());
                    }
                    formData.setStreet2(String.join("/", addressDetails));
                    formData.setZipCode(e.getShipZipcode());
                    formData.setCity(e.getShipCountry());
                    return formData;
                }).toList();
    }
    
    public List<FormData> mapFormData(List<FormDto> aFormDtos) {
        return aFormDtos.stream().map(e -> {
            FormData formData = new FormData();
            formData.setFieldValue("GondKod", "31-059");
            formData.setFieldValue("GondSehir", "Kraków/Poland");
            formData.setFieldValue("GondAd", "Neslihan Karatas");
            formData.setFieldValue("GondAdres1", "Bożego Ciała 9/7");
            formData.setFieldValue("GondAdres2", "");
            formData.setFieldValue("AlAd", e.getName());
            formData.setFieldValue("AlAdres1", e.getStreet1());
            formData.setFieldValue("AlAdres2", e.getStreet2());
            formData.setFieldValue("AlKod", e.getZipCode());
            formData.setFieldValue("AlSehir", e.getCity());
            return formData;
        }).toList();
    }
    
}
