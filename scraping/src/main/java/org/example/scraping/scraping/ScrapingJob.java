package org.example.scraping.scraping;

import org.example.scraping.parsing.AIParsing;
import org.example.scraping.persistence.repositories.CallRepository;
import org.example.scraping.scraping.ffg.FFGPdfParsing;
import org.example.scraping.scraping.ffg.FFGPdfScraping;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

@Component
public class ScrapingJob {

    private final CallRepository callRepository;

    public ScrapingJob(CallRepository callRepository) {
        this.callRepository = callRepository;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void initScraping() throws IOException {
//        List<String> grantCallUrls = FFGCallSearch.search("https://www.ffg.at/foerderungen?custom_search_date[3]=3");
//        grantCallUrls.forEach(System.out::println);
//        List<Path> scrapedPdfs = FFGPdfScraping.scrapePdfs(grantCallUrls.get(0));
        List<Path> scrapedPdfs = FFGPdfScraping.scrapePdfs("https://www.ffg.at/Breitband2030/GigaApp/2AS");

        scrapedPdfs.forEach(System.out::println);
        List<Path> extractDirPaths = FFGPdfParsing.parsePdf(scrapedPdfs);
        extractDirPaths.forEach(System.out::println);

        for (Path extractDirPath : extractDirPaths) {
            AIParsing.initParsing(extractDirPath);
        }

    }

}
