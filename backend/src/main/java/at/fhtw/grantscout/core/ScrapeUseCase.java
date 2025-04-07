package at.fhtw.grantscout.core;

import at.fhtw.grantscout.core.domain.enums.Institute;
import at.fhtw.grantscout.core.ports.out.ForContentScraping;
import at.fhtw.grantscout.out.persistence.entities.Call;
import at.fhtw.grantscout.out.persistence.entities.ScrapedCall;
import at.fhtw.grantscout.out.persistence.repositories.CallRepository;
import at.fhtw.grantscout.out.persistence.repositories.ScrapedCallRepository;
import at.fhtw.grantscout.out.scraping.eu.EUContentScraper;
import at.fhtw.grantscout.out.search.eu.EUCallSearch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class ScrapeUseCase {

    private final Logger logger = LoggerFactory.getLogger(ScrapeUseCase.class);

    private final EUContentScraper euContentScraper;

    private final CallRepository callRepository;
    private final ScrapedCallRepository scrapedCallRepository;

    public ScrapeUseCase(EUContentScraper euContentScraper, CallRepository callRepository, ScrapedCallRepository scrapedCallRepository) {
        this.euContentScraper = euContentScraper;
        this.callRepository = callRepository;
        this.scrapedCallRepository = scrapedCallRepository;
    }

    public void scrape(List<Long> callIds) {
        List<Call> calls = callRepository.findAllById(callIds);
        calls.forEach(this::callScraper);
    }

    private void callScraper(Call call) {
        try {
            logger.debug("Scraping call {}", call.getUrl());
            String content = getScraperByInstitute(call.getInstitute()).scrapeContent(call.getUrl());

            ScrapedCall scrapedCall = ScrapedCall.builder()
                    .call(call)
                    .id(call.getId())
                    .scrapedAt(LocalDateTime.now())
                    .content(content)
                    .build();
            scrapedCallRepository.save(scrapedCall);
        }
        catch (Exception e) {
            logger.error("Error while scraping call {}", call.getUrl(), e);
        }
    }

    private ForContentScraping getScraperByInstitute(Institute institute) {
        return switch(institute) {
            case EU -> euContentScraper;
            case FFG -> null; // TODO: Implement FFG scraper in this architecture
        };
    }

}
