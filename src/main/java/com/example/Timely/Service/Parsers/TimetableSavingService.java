package com.example.Timely.Service.Parsers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Timely.Models.ClassSlot;
import com.example.Timely.Repository.ClassSlotRepo;
import com.example.Timely.Service.Parsers.TimetableParser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

@Service
public class TimetableSavingService {

    private static final Logger logger = LoggerFactory.getLogger(TimetableSavingService.class);

    private final TimetableParser timetableParser;
    private final ClassSlotRepo classSlotRepo;

    public TimetableSavingService(TimetableParser timetableParser, ClassSlotRepo classSlotRepo) {
        this.timetableParser = timetableParser;
        this.classSlotRepo = classSlotRepo;
    }

    public void processAndSaveAllHtmlTimetables() {
        logger.info("--- Starting timetable processing service for all HTML files ---");
        Path dirPath = Paths.get("htmTables");

        if (!Files.isDirectory(dirPath)) {
            logger.error("CRITICAL: Timetable directory not found at -> {}", dirPath.toAbsolutePath());

            return;
        }

        try (Stream<Path> paths = Files.walk(dirPath)) {
            paths
                    .filter(path -> !Files.isDirectory(path))
                    .filter(path -> {
                        String lowerCasePath = path.toString().toLowerCase();
                        return lowerCasePath.endsWith(".htm") || lowerCasePath.endsWith(".html");
                    })
                    .forEach(this::processSingleFile);
        } catch (IOException e) {
            logger.error("An error occurred while accessing the timetable directory: {}", e.getMessage(), e);
        }
        logger.info("--- Finished timetable processing service ---");
    }

    private void processSingleFile(Path filePath) {
        logger.info("===========================================================");
        logger.info("▶️ Processing file: {}", filePath.getFileName());
        logger.info("===========================================================");
        try {
            String htmlData = Files.readString(filePath);
            List<ClassSlot> slots = timetableParser.parseHtml(htmlData);

            if (slots.isEmpty()) {
                logger.warn("Parsing complete for {}, but no class slots were extracted. Check file format or content.",
                        filePath.getFileName());
            } else {
                classSlotRepo.saveAll(slots);
                logger.info("✅ Successfully parsed and saved {} class slots from {}.", slots.size(),
                        filePath.getFileName());
            }
        } catch (IOException e) {
            logger.error("An error occurred while reading the file {}: {}", filePath.getFileName(), e.getMessage(), e);
        } catch (Exception e) {
            logger.error("An unexpected error occurred while processing file {}: {}", filePath.getFileName(),
                    e.getMessage(), e);
        }
    }
}
