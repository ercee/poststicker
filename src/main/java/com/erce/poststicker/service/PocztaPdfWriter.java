package com.erce.poststicker.service;

import java.io.*;
import java.nio.file.*;
import java.util.List;
import java.util.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import com.erce.poststicker.model.FormData;
import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.forms.fields.PdfFormField;
import com.itextpdf.io.font.*;
import com.itextpdf.kernel.font.*;
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.*;

import static com.erce.poststicker.model.FormData.AL_AD;

@Service
public class PocztaPdfWriter {
    
    public File write(List<FormData> aFormDataList) {
        String tempDir = System.getProperty("java.io.tmpdir");
        Path tempFolderPath = Paths.get(tempDir, "posta-temp", "output");
        if (!Files.exists(tempFolderPath)) {
            try {
                Files.createDirectories(tempFolderPath);
            } catch (IOException aE) {
                throw new RuntimeException(aE);
            }
        }
        for (FormData formData : aFormDataList) {
            generateFile(formData, tempFolderPath);
        }
        return mergePdfs(tempFolderPath);
    }
    
    private void generateFile(FormData formData, Path tempFolderPath) {
        try (PdfReader reader = new PdfReader("postaform.pdf"); PdfWriter writer = new PdfWriter(tempFolderPath.toFile().getPath() + File.separator + formData.getFieldValue(AL_AD) + ".pdf"); PdfDocument pdf = new PdfDocument(reader, writer)) {
            PdfAcroForm form = PdfAcroForm.getAcroForm(pdf, true);
            for (Map.Entry<String, String> entry : formData.getFieldMap().entrySet()) {
                String fieldName = entry.getKey();
                String fieldValue = entry.getValue();
                PdfFormField field = form.getField(fieldName);
                field.setFontSize(10);
                field.setValue(fieldValue);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    
    public File mergePdfs(Path aTempFolderPath) {
        File outputFile = aTempFolderPath.getParent().resolve("topluPdf" + +System.currentTimeMillis() + ".pdf").toFile();
        try (PdfWriter writer = new PdfWriter(outputFile); PdfDocument pdf = new PdfDocument(writer)) {
            File klasor = aTempFolderPath.toFile();
            for (File dosya : klasor.listFiles()) {
                if (dosya.isFile() && dosya.getName().endsWith(".pdf")) {
                    PdfDocument sourcePdf = new PdfDocument(new PdfReader(dosya));
                    sourcePdf.copyPagesTo(1, sourcePdf.getNumberOfPages(), pdf);
                    sourcePdf.close();
                }
            }
        } catch (IOException aE) {
            throw new RuntimeException(aE);
        }
        try {
            Files.walk(aTempFolderPath).sorted(Comparator.reverseOrder()).map(Path::toFile).forEach(File::delete);
        } catch (IOException aE) {
            throw new RuntimeException(aE);
        }
        return outputFile;
    }
    
    public File writeAdressesOnly(List<FormData> aFormDataList) {
        try {
            String tempDir = System.getProperty("java.io.tmpdir");
            Path tempFolderPath = Paths.get(tempDir, "posta-temp", "adresses", System.currentTimeMillis() + ".pdf");
            Path tempFolderFolder = Paths.get(tempDir, "posta-temp", "adresses");
            if (!Files.exists(tempFolderFolder)) {
                try {
                    Files.createDirectories(tempFolderFolder);
                } catch (IOException aE) {
                    throw new RuntimeException(aE);
                }
            }
            File file = tempFolderPath.toFile();
            PdfWriter writer = new PdfWriter(file);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);
            
            byte[] fontContents = new ClassPathResource("fonts/FreeSans.ttf").getContentAsByteArray();
            FontProgram fontProgram = FontProgramFactory.createFont(fontContents);
            PdfFont font = PdfFontFactory.createFont(fontProgram, "Cp1250");
            Table table = new Table(2);
            // Add the table to the document
            for (FormData formData : aFormDataList) {
                String formattedString = String.format("NADAWCA: Neslihan Karatas%sBożego Ciała 9/7 31-059 Kraków/Poland%sADRESAT: %s%s%s %s %s %s %s", System.lineSeparator(), System.lineSeparator(), formData.getFieldValue("AlAd"), System.lineSeparator(), formData.getFieldValue("AlAdres1"), formData.getFieldValue("AlAdres2"), formData.getFieldValue("AlKod"), formData.getFieldValue("AlSehir"), System.lineSeparator());
                table.addCell(new Cell().add(new Paragraph(formattedString).setFont(font).setFontSize(9)));
            }
            document.add(table);
            document.close();
            return file;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
}
