package org.example.scraping.scraping.ffg;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FFGPdfParsing {

    public static List<Path> parsePdf(List<Path> pdfPaths) throws IOException{
        File resourcesFolder = new File("src/main/resources");
//        File[] pdfFiles = resourcesFolder.listFiles((dir, name) -> name.toLowerCase().endsWith(".pdf"));
        List<File> pdfFiles = pdfPaths.stream().map(Path::toFile).toList();

        String[] exclusionKeywords = {"Inhaltsverzeichnis", "Tabellenverzeichnis", "Vorwort", "......."};

        List<Path> extractDirPaths = new ArrayList<>();
        if (!pdfFiles.isEmpty()) {
            for (File pdfFile : pdfFiles) {
                String fileNameWithoutExtension = pdfFile.getName().replaceFirst("\\.pdf$", "");
                Path outputDirPath = Paths.get(System.getProperty("user.dir"), "output", fileNameWithoutExtension);
                extractDirPaths.add(outputDirPath);

                outputDirPath.toFile().mkdirs();

                String extractedText = extractRelevantText(pdfFile, exclusionKeywords);
                saveTextToTxt(extractedText, Paths.get(outputDirPath.toString(), "/filtered_extracted.txt"));

                System.out.println("Filtered text saved to folder: " + outputDirPath);
            }
        }

        return extractDirPaths;
    }

    public static boolean containsKeyword(String text, String[] keywords) {
        for (String keyword : keywords) {
            if (text.toLowerCase().contains(keyword.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    public static String extractRelevantText(File pdfFile, String[] exclusionKeywords) throws IOException {
        PDDocument document = Loader.loadPDF(pdfFile);
        PDFTextStripper stripper = new PDFTextStripper();
        StringBuilder extractedText = new StringBuilder();

        int totalPages = document.getNumberOfPages();
        boolean isRelevantSection = false;

        for (int i = 2; i <= totalPages; i++) {
            stripper.setStartPage(i);
            stripper.setEndPage(i);

            String pageText = stripper.getText(document);

            if (containsKeyword(pageText, exclusionKeywords)) {
                System.out.println("Skipping page " + i + " due to exclusion keywords.");
                continue;
            }

            if (pageText.toLowerCase().contains("das wichtigste in kürze".toLowerCase())) {
                isRelevantSection = true;
            }

            if (isRelevantSection) {
                extractedText.append(pageText).append("\n\n");
                if (!pageText.toLowerCase().contains("das wichtigste in kürze".toLowerCase())) {
                    break; // Stop after extracting the relevant section
                }
            }
        }
        document.close();
        return extractedText.toString();
    }

    public static void saveTextToTxt(String text, Path filePath) throws IOException {
        FileWriter writer = new FileWriter(filePath.toFile().getAbsolutePath());
        writer.write(text);
        writer.close();
    }

}
