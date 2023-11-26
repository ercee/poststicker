package com.erce.poststicker.service;

import java.io.*;
import java.util.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.erce.poststicker.model.Order;
import org.apache.commons.csv.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

@Service
public class EtsyOrderReader {
    private String sFileName = "etsy.xlsx";
    
    public List<Order> readOrdersFromExcel(MultipartFile file) {
        List<Order> orders = new ArrayList<>();
        try (InputStream inputStream = file.getInputStream(); Workbook workbook = new XSSFWorkbook(inputStream)) {
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();
            if (rowIterator.hasNext()) {
                rowIterator.next();
            }
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                Order order = mapRowToOrder(row);
                orders.add(order);
            }
        } catch (IOException aE) {
            throw new RuntimeException(aE);
        }
        return orders;
    }
    
    private static Order mapRowToOrder(Row row) {
        Order order = new Order();
        order.setSaleDate(getStringCellValue(row, 0));
        order.setOrderId(getStringCellValue(row, 1));
        order.setBuyerUserId(getStringCellValue(row, 2));
        order.setFullName(getStringCellValue(row, 3));
        order.setFirstName(getStringCellValue(row, 4));
        order.setLastName(getStringCellValue(row, 5));
        order.setNumberOfItems(getStringCellValue(row, 6));
        order.setPaymentMethod(getStringCellValue(row, 7));
        order.setDateShipped(getStringCellValue(row, 8));
        order.setStreet1(getStringCellValue(row, 9));
        order.setStreet2(getStringCellValue(row, 10));
        order.setShipCity(getStringCellValue(row, 11));
        order.setShipState(getStringCellValue(row, 12));
        order.setShipZipcode(getStringCellValue(row, 13));
        order.setShipCountry(getStringCellValue(row, 14));
        order.setCurrency(getStringCellValue(row, 15));
        order.setCouponCode(getStringCellValue(row, 17));
        order.setCouponDetails(getStringCellValue(row, 18));
        order.setSalesTax(getStringCellValue(row, 22));
        order.setStatus(getStringCellValue(row, 24));
        order.setBuyer(getStringCellValue(row, 30));
        order.setOrderType(getStringCellValue(row, 31));
        order.setPaymentType(getStringCellValue(row, 32));
        order.setInPersonDiscount(getStringCellValue(row, 33));
        order.setInPersonLocation(getStringCellValue(row, 34));
        order.setSku(getStringCellValue(row, 35));
        return order;
    }
    
    
    private static String getStringCellValue(Row row, int aI) {
        Cell cell = row.getCell(aI);
        return getCellValueAsString(cell);
    }
    
    private static String getCellValueAsString(Cell cell) {
        if (cell != null) {
            switch (cell.getCellType()) {
                case STRING:
                    return cell.getStringCellValue();
                case NUMERIC:
                    // Check if the numeric value is a date
                    if (DateUtil.isCellDateFormatted(cell)) {
                        return cell.getDateCellValue().toString();
                    } else {
                        // If it's not a date, convert the numeric value to a string
                        double numericCellValue = cell.getNumericCellValue();
                        return String.valueOf((int) numericCellValue);
                    }
                case BOOLEAN:
                    return String.valueOf(cell.getBooleanCellValue());
                case FORMULA:
                    // Handle formulas as needed
                    return cell.getCellFormula();
                default:
                    return ""; // or handle it in a way that makes sense for your application
            }
        } else {
            return "";
        }
    }
    
    public List<Order> readOrdersFromCSV(MultipartFile file) {
        List<Order> orders = new ArrayList<>();
        try (Reader reader = new InputStreamReader(file.getInputStream());
             CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withHeader())) {
            
            for (CSVRecord csvRecord : csvParser) {
                Order order = mapCsvRecordToOrder(csvRecord);
                orders.add(order);
            }
            
        } catch (IOException e) {
            throw new RuntimeException("Error reading orders from CSV", e);
        }
        return orders;
    }
    
    private Order mapCsvRecordToOrder(CSVRecord aCsvRecord) {
        Order order = new Order();
        order.setSaleDate(aCsvRecord.get(0));
        order.setOrderId(aCsvRecord.get(1));
        order.setBuyerUserId(aCsvRecord.get(2));
        order.setFullName(aCsvRecord.get(3));
        order.setFirstName(aCsvRecord.get(4));
        order.setLastName(aCsvRecord.get(5));
        order.setNumberOfItems(aCsvRecord.get(6));
        order.setPaymentMethod(aCsvRecord.get(7));
        order.setDateShipped(aCsvRecord.get(8));
        order.setStreet1(aCsvRecord.get(9));
        order.setStreet2(aCsvRecord.get(10));
        order.setShipCity(aCsvRecord.get(11));
        order.setShipState(aCsvRecord.get(12));
        order.setShipZipcode(aCsvRecord.get(13));
        order.setShipCountry(aCsvRecord.get(14));
        order.setCurrency(aCsvRecord.get(15));
        order.setCouponCode(aCsvRecord.get(17));
        order.setCouponDetails(aCsvRecord.get(18));
        order.setSalesTax(aCsvRecord.get(22));
        order.setStatus(aCsvRecord.get(24));
        order.setBuyer(aCsvRecord.get(30));
        order.setOrderType(aCsvRecord.get(31));
        order.setPaymentType(aCsvRecord.get(32));
        order.setInPersonDiscount(aCsvRecord.get(33));
        order.setInPersonLocation(aCsvRecord.get(34));
        order.setSku(aCsvRecord.get(35));
        return order;
    }
    
}
