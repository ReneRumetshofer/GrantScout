package at.fhtw.grantscout.core;

import at.fhtw.grantscout.out.persistence.repositories.CallRepository;
import org.springframework.stereotype.Component;

@Component
public class ParseCallUseCase {

    private final CallRepository callRepository;

    public ParseCallUseCase(CallRepository callRepository) {
        this.callRepository = callRepository;
    }

    public void parseCall(Long scrapedCallId) {

    }

}
