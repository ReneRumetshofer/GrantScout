package at.fhtw.grantscout.core.factories;

import at.fhtw.grantscout.core.domain.data.CallData;
import at.fhtw.grantscout.core.domain.data.ScrapedCallData;
import at.fhtw.grantscout.out.persistence.entities.ScrapedCall;
import org.springframework.stereotype.Component;

@Component
public class ScrapedCallDataFactory {

    private final CallDataFactory callDataFactory;

    public ScrapedCallDataFactory(CallDataFactory callDataFactory) {
        this.callDataFactory = callDataFactory;
    }

    public ScrapedCallData fromDbEntity(ScrapedCall scrapedCall) {
        return ScrapedCallData.builder()
                .call(callDataFactory.fromDbEntity(scrapedCall.getCall()))
                .scrapedAt(scrapedCall.getScrapedAt())
                .content(scrapedCall.getContent())
                .build();
    }

}
