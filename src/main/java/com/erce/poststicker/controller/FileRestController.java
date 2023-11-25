package com.erce.poststicker.controller;

import java.util.List;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.erce.poststicker.model.*;
import com.erce.poststicker.service.*;

@RestController
public class FileRestController {
    private final EtsyOrderReader mEtsyOrderReader;
    private final OrderFormMapper mOrderFormMapper;
    
    public FileRestController(EtsyOrderReader aEtsyOrderReader, OrderFormMapper aOrderFormMapper) {
        mEtsyOrderReader = aEtsyOrderReader;
        mOrderFormMapper = aOrderFormMapper;
    }
    
    @PostMapping("/saves")
    public List<FormDto> loadOrders(@RequestPart("file") MultipartFile file) {
        List<Order> orders = mEtsyOrderReader.readOrdersFromExcel(file);
        return mOrderFormMapper.map(orders);
    }
}
