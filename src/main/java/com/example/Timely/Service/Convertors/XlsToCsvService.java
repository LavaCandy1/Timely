package com.example.Timely.Service.Convertors;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class XlsToCsvService {

    @Value("${file.converter.input-dir}")
    private String inputDir;

    @Value("${file.converter.output-dir}")
    private String outputDir;

    public void processAllXlsFiles() {
        System.out.println("Starting XLS to CSV conversion process...");
        File inputDirectory = new File(inputDir);
        
        // Ensure directories exist
        if (!inputDirectory.exists()) {
            System.err.println("Input directory does not exist: " + inputDir);
            return;
        }
        try {
            Files.createDirectories(Paths.get(outputDir));
        } catch (IOException e) {
            System.err.println("Could not create output directory: " + e.getMessage());
            return;
        }

        File[] xlsFiles = inputDirectory.listFiles();
        if (xlsFiles == null) {
            System.err.println("Could not read files from directory: " + inputDir);
            return;
        }

        int convertedCount = 0;
        for (File file : xlsFiles) {
            String fileName = file.getName().toLowerCase();
            // Look for .xls files now
            if (file.isFile() && fileName.endsWith(".xls")) {
                try {
                    convertSingleXlsToCsv(file);
                    convertedCount++;
                } catch (IOException e) {
                    System.err.println("Error converting file " + file.getName() + ": " + e.getMessage());
                }
            }
        }
        System.out.println("Conversion process finished. Files converted: " + convertedCount);
    }

    private void convertSingleXlsToCsv(File xlsFile) throws IOException {
        String baseName = xlsFile.getName().substring(0, xlsFile.getName().lastIndexOf('.'));
        Path outputPath = Paths.get(outputDir, baseName + ".csv");

        try (FileInputStream fis = new FileInputStream(xlsFile);
             Workbook workbook = new HSSFWorkbook(fis);
             BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath.toFile()))) {
            
            DataFormatter dataFormatter = new DataFormatter();

            for (Sheet sheet : workbook) {
                for (Row row : sheet) {
                    StringBuilder csvRow = new StringBuilder();
                    for (int i = 0; i < row.getLastCellNum(); i++) {
                        Cell cell = row.getCell(i, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                        
                        String cellValue = "";
                        if (cell != null) {
                            cellValue = dataFormatter.formatCellValue(cell);
                        }
                        
                        csvRow.append(escapeCsv(cellValue));
                        if (i < row.getLastCellNum() - 1) {
                            csvRow.append(",");
                        }
                    }
                    writer.write(csvRow.toString());
                    writer.newLine();
                }
            }
        }
        System.out.println("Successfully converted " + xlsFile.getPath() + " to " + outputPath);
    }

    private String escapeCsv(String text) {
        if (text.contains(",") || text.contains("\"") || text.contains("\n")) {
            text = text.replace("\"", "\"\"");
            return "\"" + text + "\"";
        }
        return text;
    }
}