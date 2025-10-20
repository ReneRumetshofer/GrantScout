package at.fhtw.grantscout.core;

import at.fhtw.grantscout.core.domain.data.call.ParsedCallData;
import at.fhtw.grantscout.core.domain.data.call.ScrapedCallData;
import at.fhtw.grantscout.core.domain.enums.CallStatus;
import at.fhtw.grantscout.core.factories.ParsedDataFactory;
import at.fhtw.grantscout.core.factories.ScrapedCallDataFactory;
import at.fhtw.grantscout.out.persistence.repositories.ParsedCallRepository;
import at.fhtw.grantscout.out.persistence.repositories.ScrapedCallRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FindCallsUseCase {

    private final ScrapedCallRepository scrapedCallRepository;
    private final ScrapedCallDataFactory scrapedCallDataFactory;
    private final ParsedCallRepository parsedCallRepository;
    private final ParsedDataFactory parsedDataFactory;

    public FindCallsUseCase(ScrapedCallRepository scrapedCallRepository, ScrapedCallDataFactory scrapedCallDataFactory, ParsedCallRepository parsedCallRepository, ParsedDataFactory parsedDataFactory) {
        this.scrapedCallRepository = scrapedCallRepository;
        this.scrapedCallDataFactory = scrapedCallDataFactory;
        this.parsedCallRepository = parsedCallRepository;
        this.parsedDataFactory = parsedDataFactory;
    }

    public List<ScrapedCallData> findAllScrapedCalls() {
        return scrapedCallRepository.findAllByCall_Status(CallStatus.SCRAPED)
                .stream()
                .map(scrapedCallDataFactory::fromDbEntity)
                .toList();
    }

    public List<ParsedCallData> findAllParsedCalls() {
        return parsedCallRepository.findAllByCall_Status(CallStatus.PARSED)
                .stream()
                .map(parsedDataFactory::fromDbEntity)
                .toList();
    }

}
