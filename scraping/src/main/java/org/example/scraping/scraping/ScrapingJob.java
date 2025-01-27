package org.example.scraping.scraping;

import org.example.scraping.persistence.entities.Call;
import org.example.scraping.persistence.repositories.CallRepository;
import org.example.scraping.scraping.ffg.FFGCallSearch;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class ScrapingJob {

    private final CallRepository callRepository;

    public ScrapingJob(CallRepository callRepository) {
        this.callRepository = callRepository;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void initScraping() {
        List<String> grantCallUrls = FFGCallSearch.search("https://www.ffg.at/foerderungen?custom_search_date[3]=3");

        for(String grantCall : grantCallUrls) {
            Optional<Call> persistedCall = callRepository.findByUrl(grantCall);
            if (persistedCall.isEmpty()) {
                Call call = Call.builder()
                        .url(grantCall)
                        .institute("FFG")
                        .scraped(false)
                        .build();
                callRepository.save(call);
            }
        }

    }

}
