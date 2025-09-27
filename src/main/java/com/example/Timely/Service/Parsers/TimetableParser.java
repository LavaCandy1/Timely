package com.example.Timely.Service.Parsers;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import com.example.Timely.Models.ClassSlot;

@Component
public class TimetableParser {

    private final Pattern entryPattern = Pattern.compile(
            "([\\w\\d\\s\\/.-]+?):\\s*(.*?)(?:,\\s*)?\\(([LPT])\\):\\s*(.*?)\\s*\\{(.*?)\\}");

    public List<ClassSlot> parseCsv(String csvData, String fileName) {
        List<ClassSlot> classSlots = new ArrayList<>();

        // Metadata extraction from filename
        Pattern filePattern = Pattern.compile("(\\d{4}-\\d{2})-(even|odd)_BTECH_([A-Za-z]+)_([A-Z]\\d+)");
        Matcher fileMatcher = filePattern.matcher(fileName);

        String session = "", semester = "", year = "", mainBatch = "";
        if (fileMatcher.find()) {
            session = fileMatcher.group(1);
            semester = fileMatcher.group(2);
            year = fileMatcher.group(3);
            mainBatch = fileMatcher.group(4);
            semester = semester.substring(0, 1).toUpperCase() + semester.substring(1);
            year = year.substring(0, 1).toUpperCase() + year.substring(1);
        } else {
            System.err.println("Warning: Could not parse all metadata from filename: " + fileName);
        }

        String[] lines = csvData.trim().split("\\r?\\n|\\r");
        int headerRowIndex = findHeaderRowIndex(lines);
        if (headerRowIndex == -1) return classSlots;

        String[] headers = lines[headerRowIndex].split(",", -1);

        for (int i = headerRowIndex + 1; i < lines.length; i++) {
            String[] cells = lines[i].split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
            if (cells.length <= 1) continue;

            String timeRange = cells[0].replace("\"", "").trim();
            if (timeRange.isEmpty()) continue;

            String[] times = timeRange.split("-");
            if (times.length < 2) continue;
            String startTime = times[0].trim(), endTime = times[1].trim();

            for (int j = 1; j < cells.length; j++) {
                if (j >= headers.length) continue;
                String dayOfWeek = headers[j].trim();
                String cellContent = cells[j].replace("\"", "").trim();
                if (cellContent.isEmpty() || dayOfWeek.isEmpty()) continue;

                extractSlotsFromCell(cellContent, dayOfWeek, startTime, endTime, mainBatch, year, semester, session, classSlots);
            }
        }
        return classSlots;
    }

    /**
     * Parses an HTML string of a timetable into a list of ClassSlot objects.
     *
     * @param htmlData The string containing the HTML table.
     * @return A list of ClassSlot objects.
     */
    public List<ClassSlot> parseHtml(String htmlData) {
        List<ClassSlot> classSlots = new ArrayList<>();
        Document doc = Jsoup.parse(htmlData);

        // Extract metadata from headers
        String metaText = doc.select("td[colspan=7] b").text();
        String session = extractFileInfo(metaText, "(\\d{4}-\\d{2})");
        String semester = extractFileInfo(metaText, "-(even|odd)");
        String mainBatch = extractFileInfo(metaText, "Batch:\\s*(\\w+)");
        String year = extractFileInfo(metaText, "\\(BTECH-([^)]+)\\)");

        if (!semester.isEmpty()) semester = semester.substring(0, 1).toUpperCase() + semester.substring(1);
        if (!year.isEmpty()) year = year.substring(0, 1).toUpperCase() + year.substring(1);

        Element table = doc.select("table").first();
        if (table == null) return classSlots;

        Elements rows = table.select("tr");
        int headerRowIndex = findHeaderRowIndex(rows.stream().map(Element::text).toArray(String[]::new));
        if (headerRowIndex == -1) return classSlots;

        String[] headers = rows.get(headerRowIndex).select("td, th").stream()
                                 .map(Element::text).map(String::trim).toArray(String[]::new);

        for (int i = headerRowIndex + 1; i < rows.size(); i++) {
            Elements cells = rows.get(i).select("td, th");
            if (cells.size() <= 1) continue;

            String timeRange = cells.first().text().trim();
            if (timeRange.isEmpty()) continue;
            
            String[] times = timeRange.split("-");
            if (times.length < 2) continue;
            String startTime = times[0].trim(), endTime = times[1].trim();

            for (int j = 1; j < cells.size(); j++) {
                if (j >= headers.length) continue;
                String dayOfWeek = headers[j];
                String cellContent = cells.get(j).html().replaceAll("(?i)<br[^>]*>", " ");
                cellContent = Jsoup.parse(cellContent).text().trim();

                if (cellContent.isEmpty() || dayOfWeek.isEmpty()) continue;

                extractSlotsFromCell(cellContent, dayOfWeek, startTime, endTime, mainBatch, year, semester, session, classSlots);
            }
        }
        return classSlots;
    }

    /**
     * Shared logic to extract all class entries from a single cell's text content.
     */
    private void extractSlotsFromCell(String cellContent, String dayOfWeek, String startTime, String endTime, String mainBatch, String year, String semester, String session, List<ClassSlot> classSlots) {
        Matcher matcher = entryPattern.matcher(cellContent);
        while (matcher.find()) {
            String combinedCourseCodes = matcher.group(1).trim();
            String batchAndGroups = matcher.group(2).trim();
            String slotTypeChar = matcher.group(3).trim();
            String instructor = matcher.group(4).trim();
            String location = matcher.group(5).trim();

            // --- BATCH FILTERING LOGIC ---
            boolean isBatchSpecific = batchAndGroups.matches(".*\\bB\\d+\\b.*");
            boolean containsMainBatch = batchAndGroups.matches(".*\\b" + Pattern.quote(mainBatch) + "\\b.*");
            boolean isForAll = batchAndGroups.toUpperCase().contains("ALL");

            if (isBatchSpecific && !containsMainBatch && !isForAll) {
                continue; // Skip this entry, it's for another batch.
            }
            // --- END OF FILTERING LOGIC ---

            String[] courseCodes = combinedCourseCodes.split("/");
            for (String courseCode : courseCodes) {
                ClassSlot.SlotType slotType = mapSlotType(slotTypeChar);
                // --- CORRECTED LOGIC ---
                // The batch is always the mainBatch from the header.
                // We only check the cell's description for group/section info (S1, G1, etc.).
                String groups = "";
                if (!batchAndGroups.isEmpty()) {
                    String firstChar = batchAndGroups.toUpperCase().substring(0, 1);
                    if (firstChar.equals("S") || firstChar.equals("G")) {
                        groups = batchAndGroups;
                    }
                }
                
                // Assuming the full constructor from previous context is correct
                classSlots.add(new ClassSlot(courseCode.trim(), dayOfWeek, startTime, endTime, location, instructor, slotType, mainBatch, groups, year));
            }
        }
    }

    private int findHeaderRowIndex(String[] lines) {
        for (int i = 0; i < lines.length; i++) {
            String trimmedLine = lines[i].trim().toLowerCase();
            if ((trimmedLine.startsWith(",") || trimmedLine.startsWith("monday")) && trimmedLine.contains("monday")) {
                return i;
            }
        }
        System.err.println("Error: Could not find a valid header row.");
        return -1;
    }

    private ClassSlot.SlotType mapSlotType(String type) {
        return switch (type.toUpperCase()) {
            case "L" -> ClassSlot.SlotType.LECTURE;
            case "P" -> ClassSlot.SlotType.LAB;
            default -> ClassSlot.SlotType.TUTORIAL;
        };
    }

    private String extractFileInfo(String text, String regex) {
        Matcher matcher = Pattern.compile(regex, Pattern.CASE_INSENSITIVE).matcher(text);
        return matcher.find() ? matcher.group(1).trim() : "";
    }
}
