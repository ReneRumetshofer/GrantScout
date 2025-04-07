package at.fhtw.grantscout.in;

import at.fhtw.grantscout.core.CallSearchUseCase;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class ScrapingJob {

    private final CallSearchUseCase callSearchUseCase;

    public ScrapingJob(CallSearchUseCase callSearchUseCase) {
        this.callSearchUseCase = callSearchUseCase;
    }

    @Scheduled(fixedRateString = "${scraping.interval_minutes}", timeUnit = TimeUnit.MINUTES)
    public void initScraping() {
        callSearchUseCase.conductSearch();
    }

}
