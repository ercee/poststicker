package com.erce.poststicker.controller;

import java.io.*;
import java.nio.file.Files;
import java.util.List;
import org.springframework.core.io.*;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.erce.poststicker.model.*;
import com.erce.poststicker.service.*;

@Controller
public class FileController {
    private final EtsyOrderReader mEtsyOrderReader;
    private final OrderFormMapper mOrderFormMapper;
    private final PocztaPdfWriter mPocztaPdfWriter;
    
    public FileController(EtsyOrderReader aEtsyOrderReader, OrderFormMapper aOrderFormMapper, PocztaPdfWriter aPocztaPdfWriter) {
        mEtsyOrderReader = aEtsyOrderReader;
        mOrderFormMapper = aOrderFormMapper;
        mPocztaPdfWriter = aPocztaPdfWriter;
    }
    
    @GetMapping("")
    public String indexSayfasi() {
        return "redirect:/import";
    }
    
    @GetMapping("/import")
    public String importFile() {
        return "import-file";
    }
    
    @PostMapping("/save")
    public String formList(Model model, @RequestPart("file") MultipartFile file) {
        List<Order> orders = mEtsyOrderReader.readOrdersFromExcel(file);
        List<FormDto> formDtoList = mOrderFormMapper.map(orders);
        
        // Model'e formDtoList'i ekle
        SubmitForm attributeValue = new SubmitForm();
        attributeValue.setFormDtoList(formDtoList);
        model.addAttribute("submitForm", attributeValue);
        model.addAttribute("formDtoList", formDtoList);
        
        return "form-list-template";
    }
    
    @PostMapping("/submit-form")
    public ResponseEntity<Resource> submitForm(@ModelAttribute("submitForm") SubmitForm submitForm) {
        List<FormDto> formDtoList = submitForm.getFormDtoList().stream().filter(FormDto::isSelected).toList();
        List<FormData> formDataList = mOrderFormMapper.mapFormData(formDtoList);
        if (!submitForm.isOnlyAdresses()) {
            return createPostStickers(formDataList);
        }
        return createPostStickers(formDataList);
    }
    
    private ResponseEntity<Resource> createPostStickers(List<FormData> formDataList) {
        try {
            File pdfFile = mPocztaPdfWriter.write(formDataList);
            Resource resource = new ByteArrayResource(Files.readAllBytes(pdfFile.toPath()));
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + pdfFile.getName());
            return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF).body(resource);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }
    
}
