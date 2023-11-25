package com.erce.poststicker.controller;

import java.io.*;
import java.nio.file.Files;
import java.util.List;
import org.springframework.core.io.*;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
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
    public ResponseEntity<Resource> saveFile(@RequestPart("file") MultipartFile file) {
        try {
            // Your existing code to process the file and generate PDF
            List<Order> orders = mEtsyOrderReader.readOrdersFromExcel(file);
            List<FormData> formDataList = mOrderFormMapper.map(orders);
            File pdfFile = mPocztaPdfWriter.write(formDataList);
            
            // Create a Resource from the PDF file
            Resource resource = new ByteArrayResource(Files.readAllBytes(pdfFile.toPath()));
            
            // Set up response headers
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + pdfFile.getName());
            
            // Return the ResponseEntity with the PDF content and headers
            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(resource);
            
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception appropriately, e.g., return an error response
            return ResponseEntity.status(500).build();
        }
    }
}
