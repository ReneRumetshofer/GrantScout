package at.fhtw.grantscout.core;

import at.fhtw.grantscout.core.domain.enums.CallStatus;
import at.fhtw.grantscout.out.parsing.Parser;
import at.fhtw.grantscout.core.domain.data.ParsingResult;
import at.fhtw.grantscout.out.persistence.entities.Call;
import at.fhtw.grantscout.out.persistence.entities.ParsedCall;
import at.fhtw.grantscout.out.persistence.entities.ScrapedCall;
import at.fhtw.grantscout.out.persistence.repositories.CallRepository;
import at.fhtw.grantscout.out.persistence.repositories.ParsedCallRepository;
import at.fhtw.grantscout.out.persistence.repositories.ScrapedCallRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class ParsingPreviouslyScrapedUseCase {

    private final Logger logger = LoggerFactory.getLogger(ParsingPreviouslyScrapedUseCase.class);

    private final CallRepository callRepository;
    private final ScrapedCallRepository scrapedCallRepository;
    private final ParsedCallRepository parsedCallRepository;
    private final Parser parser;

    public ParsingPreviouslyScrapedUseCase(CallRepository callRepository, ScrapedCallRepository scrapedCallRepository, ParsedCallRepository parsedCallRepository, Parser parser) {
        this.callRepository = callRepository;
        this.scrapedCallRepository = scrapedCallRepository;
        this.parsedCallRepository = parsedCallRepository;
        this.parser = parser;
    }

    public void parsePreviouslyScrapedCalls() {
        List<ScrapedCall> callsToParse = scrapedCallRepository.findAllByCall_Status(CallStatus.SCRAPED);

        callsToParse.stream().limit(3).forEach(scrapedCall -> {
            logger.info("Parsing previously scraped call with ID {}: {}", scrapedCall.getId(), scrapedCall.getCall().getUrl());
            ParsingResult parsingResult = parser.parse(scrapedCall.getContent());

            try {
                ParsedCall newParsedCall = ParsedCall.builder()
                        .call(scrapedCall.getCall())
                        .id(scrapedCall.getCall().getId())
                        .parsedAt(LocalDateTime.now())
                        .jsonData(new ObjectMapper().writeValueAsString(parsingResult))
                        .build();
                parsedCallRepository.save(newParsedCall);
            } catch (JsonProcessingException e) {
                logger.info("Error writing parsing result to DB: {}", e.getMessage(), e);
                throw new RuntimeException(e);
            }

            scrapedCall.getCall().setStatus(CallStatus.PARSED);
            callRepository.save(scrapedCall.getCall());
        });
    }

}
