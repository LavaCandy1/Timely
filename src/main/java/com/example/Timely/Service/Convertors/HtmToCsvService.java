package com.example.Timely.Service.Convertors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class HtmToCsvService {

    @Value("${file.converter.input-dir}")
    private String inputDir;

    @Value("${file.converter.output-dir}")
    private String outputDir;

    public void processAllHtmFiles() {
        System.out.println("Starting HTM to CSV conversion process...");
        System.out.println("Input Directory: " + inputDir);
        System.out.println("Output Directory: " + outputDir);

        try {
            Files.createDirectories(Paths.get(outputDir));
        } catch (IOException e) {
            System.err.println("Could not create output directory: " + e.getMessage());
            return;
        }

        File htmDirectory = new File(inputDir);
        File[] htmFiles = htmDirectory.listFiles();

        if (htmFiles == null) {
            System.err.println("Could not read files from directory or directory is empty: " + inputDir);
            return;
        }

        int convertedCount = 0;
        for (File file : htmFiles) {
            if (file.isFile() && file.getName().toLowerCase().endsWith(".htm")) {
                try {
                    convertSingleFile(file);
                    convertedCount++;
                } catch (IOException e) {
                    System.err.println("Error converting file " + file.getName() + ": " + e.getMessage());
                }
            }
        }
        System.out.println("Conversion process finished. Files converted: " + convertedCount);
    }

    private void convertSingleFile(File htmFile) throws IOException {
        String baseName = htmFile.getName().substring(0, htmFile.getName().lastIndexOf('.'));
        Path outputPath = Paths.get(outputDir, baseName + ".csv");

        Document doc = Jsoup.parse(htmFile, "UTF-8");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath.toFile()))) {
            Elements tables = doc.select("table");
            for (Element table : tables) {
                for (Element row : table.select("tr")) {
                    StringBuilder csvRow = new StringBuilder();
                    Elements cells = row.select("th, td");
                    for (int i = 0; i < cells.size(); i++) {
                        csvRow.append(escapeCsv(cells.get(i).text()));
                        if (i < cells.size() - 1) {
                            csvRow.append(",");
                        }
                    }
                    writer.write(csvRow.toString());
                    writer.newLine();
                }
            }
        }
        System.out.println("Successfully converted " + htmFile.getPath() + " to " + outputPath);
    }

    private String escapeCsv(String text) {
        if (text.contains(",") || text.contains("\"") || text.contains("\n")) {
            text = text.replace("\"", "\"\"");
            return "\"" + text + "\"";
        }
        return text;
    }
}






// Select TT where RoomCode & DAY & Time match

