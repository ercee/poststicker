package com.erce.poststicker.service;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import org.springframework.stereotype.Service;
import com.erce.poststicker.model.FormData;
import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.forms.fields.PdfFormField;
import com.itextpdf.kernel.pdf.*;

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
        try (PdfReader reader = new PdfReader("postaform.pdf");
             PdfWriter writer = new PdfWriter(tempFolderPath.toFile().getPath() + File.separator + formData.getFieldValue(AL_AD) + ".pdf");
             PdfDocument pdf = new PdfDocument(reader, writer)) {
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
            Files.walk(aTempFolderPath)
                    .sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);
        } catch (IOException aE) {
            throw new RuntimeException(aE);
        }
        return outputFile;
    }
}
