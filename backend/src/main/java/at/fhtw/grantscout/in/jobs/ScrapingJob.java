package at.fhtw.grantscout.in.jobs;

import at.fhtw.grantscout.core.CallSearchUseCase;
import at.fhtw.grantscout.core.ParsingPreviouslyScrapedUseCase;
import at.fhtw.grantscout.core.ScrapeUseCase;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class ScrapingJob {

    private final CallSearchUseCase callSearchUseCase;
    private final ScrapeUseCase scrapeUseCase;
    private final ParsingPreviouslyScrapedUseCase parsingPreviouslyScrapedUseCase;

    public ScrapingJob(CallSearchUseCase callSearchUseCase, ScrapeUseCase scrapeUseCase, ParsingPreviouslyScrapedUseCase parsingPreviouslyScrapedUseCase) {
        this.callSearchUseCase = callSearchUseCase;
        this.scrapeUseCase = scrapeUseCase;
        this.parsingPreviouslyScrapedUseCase = parsingPreviouslyScrapedUseCase;
    }

    @Scheduled(fixedRateString = "${scraping.interval_minutes}", timeUnit = TimeUnit.MINUTES)
    public void initScraping() {
        callSearchUseCase.conductSearch();

        // Catch-alls on failure
        scrapeUseCase.scrapePreviouslyFoundCalls();
        parsingPreviouslyScrapedUseCase.parsePreviouslyScrapedCalls();
    }

}
