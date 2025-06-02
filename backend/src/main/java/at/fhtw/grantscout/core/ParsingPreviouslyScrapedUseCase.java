package at.fhtw.grantscout.core;

import at.fhtw.grantscout.core.domain.enums.CallStatus;
import at.fhtw.grantscout.out.persistence.entities.ScrapedCall;
import at.fhtw.grantscout.out.persistence.repositories.ScrapedCallRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ParsingPreviouslyScrapedUseCase {

    private final Logger logger = LoggerFactory.getLogger(ParsingPreviouslyScrapedUseCase.class);

    private final ScrapedCallRepository scrapedCallRepository;
    private final ParseCallUseCase parseCallUseCase;

    public ParsingPreviouslyScrapedUseCase(ScrapedCallRepository scrapedCallRepository, ParseCallUseCase parseCallUseCase) {
        this.scrapedCallRepository = scrapedCallRepository;
        this.parseCallUseCase = parseCallUseCase;
    }

    public void parsePreviouslyScrapedCalls() {
        List<ScrapedCall> callsToParse = scrapedCallRepository.findAllByCall_Status(CallStatus.SCRAPED);

        callsToParse.stream().limit(3).forEach(scrapedCall -> {
            logger.info("Parsing previously scraped call with ID {}: {}", scrapedCall.getId(), scrapedCall.getCall().getUrl());

        });
    }

}
