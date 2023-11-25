package com.erce.poststicker.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.erce.poststicker.model.*;

@Service
public class OrderFormMapper {
    private final PocztaPdfWriter mPocztaPdfWriter;
    private final EtsyOrderReader mEtsyOrderReader;
    
    public OrderFormMapper(PocztaPdfWriter aPocztaPdfWriter, EtsyOrderReader aEtsyOrderReader) {
        mPocztaPdfWriter = aPocztaPdfWriter;
        mEtsyOrderReader = aEtsyOrderReader;
    }
    
    public List<FormData> map(List<Order> orders) {
        return orders.stream().filter(e -> e.getDateShipped() != "")
                .map(e -> {
                    FormData formData = new FormData();
                    formData.setFieldValue("GondKod", "31-059");
                    formData.setFieldValue("GondSehir", "Kraków/Poland");
                    formData.setFieldValue("GondAd", "Neslihan Karatas");
                    formData.setFieldValue("GondAdres1", "Bożego Ciała 9/7");
                    formData.setFieldValue("GondAdres2", "");
                    formData.setFieldValue("AlAd", e.getFullName());
                    formData.setFieldValue("AlAdres1", e.getStreet1());
                    formData.setFieldValue("AlAdres2", e.getStreet2());
                    formData.setFieldValue("AlKod", e.getShipZipcode());
                    formData.setFieldValue("AlSehir", e.getShipCity() + "/" + e.getShipState() + "/" + e.getShipCountry());
                    return formData;
                }).toList();
    }
}
