package at.fhtw.grantscout.core;

import at.fhtw.grantscout.core.domain.enums.CallStatus;
import at.fhtw.grantscout.core.domain.enums.Institute;
import at.fhtw.grantscout.out.persistence.entities.Call;
import at.fhtw.grantscout.out.persistence.repositories.CallRepository;
import at.fhtw.grantscout.out.search.eu.EUCallSearch;
import at.fhtw.grantscout.out.search.ffg.FFGCallSearch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import java.util.ArrayList;
import java.util.List;

@Component
public class CallSearchUseCase {

    private final FFGCallSearch ffgCallSearch;
    private final EUCallSearch euCallSearch;
    private final CallRepository callRepository;
    private final ScrapeUseCase scrapeUseCase;

    private Logger logger = LoggerFactory.getLogger(CallSearchUseCase.class);

    private static List<Institute> activeInstitutes = List.of(
            Institute.FFG
//            Institute.EU
    );

    public CallSearchUseCase(FFGCallSearch ffgCallSearch, EUCallSearch euCallSearch, CallRepository callRepository, ScrapeUseCase scrapeUseCase) {
        this.ffgCallSearch = ffgCallSearch;
        this.euCallSearch = euCallSearch;
        this.callRepository = callRepository;
        this.scrapeUseCase = scrapeUseCase;
    }

    public void conductSearch() {
        List<Call> newlyFoundCalls = new ArrayList<>();
        activeInstitutes.forEach(institute -> {
            List<Call> calls = searchForInstitute(institute);
            newlyFoundCalls.addAll(calls);
        });

        scrapeUseCase.scrape(newlyFoundCalls.stream()
                .map(Call::getId)
                .toList());
    }

    private List<Call> searchForInstitute(Institute institute) {
        logger.debug("Starting search for {}", institute.getName());
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        List<String> urls = callInstituteSearch(institute);
        callRepository.findAll()
                .forEach(call -> urls.remove(call.getUrl()));
        logger.debug("Found {} new {} calls", institute.getName(), urls.size());
        List<Call> createdCalls = new ArrayList<>();
        for(String url : urls) {
            Call newCall = Call.builder()
                    .status(CallStatus.FOUND)
                    .url(url)
                    .institute(institute)
                    .build();
            callRepository.save(newCall);
            createdCalls.add(newCall);
        }
        stopWatch.stop();
        logger.debug("Finished {} search in {} ms", institute.getName(), stopWatch.getTotalTimeMillis());

        return createdCalls;
    }

    private List<String> callInstituteSearch(Institute institute) {
        return switch (institute) {
            case FFG -> ffgCallSearch.search(institute.getBaseUrl());
            case EU -> euCallSearch.search(institute.getBaseUrl());
        };
    }

}
