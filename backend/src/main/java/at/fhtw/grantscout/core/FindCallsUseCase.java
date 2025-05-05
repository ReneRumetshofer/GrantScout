package at.fhtw.grantscout.core;

import at.fhtw.grantscout.core.domain.data.ScrapedCallData;
import at.fhtw.grantscout.core.factories.ScrapedCallDataFactory;
import at.fhtw.grantscout.out.persistence.repositories.ScrapedCallRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FindCallsUseCase {

    private final ScrapedCallRepository scrapedCallRepository;
    private final ScrapedCallDataFactory scrapedCallDataFactory;

    public FindCallsUseCase(ScrapedCallRepository scrapedCallRepository, ScrapedCallDataFactory scrapedCallDataFactory) {
        this.scrapedCallRepository = scrapedCallRepository;
        this.scrapedCallDataFactory = scrapedCallDataFactory;
    }

    public List<ScrapedCallData> findAllScrapedCalls() {
        return scrapedCallRepository.findAll()
                .stream()
                .map(scrapedCallDataFactory::fromDbEntity)
                .toList();
    }

}
