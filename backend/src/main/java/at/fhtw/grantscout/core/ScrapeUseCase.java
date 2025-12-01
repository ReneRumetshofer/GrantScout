package at.fhtw.grantscout.core;

import at.fhtw.grantscout.core.domain.enums.CallStatus;
import at.fhtw.grantscout.core.domain.enums.Institute;
import at.fhtw.grantscout.core.events.CallScrapedEvent;
import at.fhtw.grantscout.core.ports.out.ForContentScraping;
import at.fhtw.grantscout.out.persistence.entities.Call;
import at.fhtw.grantscout.out.persistence.entities.ScrapedCall;
import at.fhtw.grantscout.out.persistence.repositories.CallRepository;
import at.fhtw.grantscout.out.persistence.repositories.ScrapedCallRepository;
import at.fhtw.grantscout.out.scraping.eu.EUContentScraper;
import at.fhtw.grantscout.out.scraping.ffg.FFGContentScraper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class ScrapeUseCase {

    private final Logger logger = LoggerFactory.getLogger(ScrapeUseCase.class);

    private final EUContentScraper euContentScraper;
    private final FFGContentScraper ffgContentScraper;

    private final CallRepository callRepository;
    private final ScrapedCallRepository scrapedCallRepository;
    private final ApplicationEventPublisher eventPublisher;

    public ScrapeUseCase(EUContentScraper euContentScraper, FFGContentScraper ffgContentScraper, CallRepository callRepository, ScrapedCallRepository scrapedCallRepository, ApplicationEventPublisher eventPublisher) {
        this.euContentScraper = euContentScraper;
        this.ffgContentScraper = ffgContentScraper;
        this.callRepository = callRepository;
        this.scrapedCallRepository = scrapedCallRepository;
        this.eventPublisher = eventPublisher;
    }

    public void scrape(List<Long> callIds) {
        List<Call> calls = callRepository.findAllById(callIds);
        calls.forEach(this::callScraper);
        logger.info("Scraped {} calls", calls.size());
    }

    public void scrapePreviouslyFoundCalls() {
        List<Call> calls = callRepository.findAllByStatus(CallStatus.FOUND);
        logger.debug("Found {} previously found calls from database to scrape", calls.size());
        calls.forEach(this::callScraper);
        logger.info("Scraped {} calls", calls.size());
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

            call.setStatus(CallStatus.SCRAPED);
            callRepository.save(call);

            eventPublisher.publishEvent(new CallScrapedEvent(this, scrapedCall.getId()));
        }
        catch (Exception e) {
            logger.error("Error while scraping call {}", call.getUrl(), e);
        }
    }

    private ForContentScraping getScraperByInstitute(Institute institute) {
        return switch(institute) {
            case EU -> euContentScraper;
            case FFG -> ffgContentScraper;
        };
    }

}
