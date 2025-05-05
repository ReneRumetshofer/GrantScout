package at.fhtw.grantscout.in.eventlistener;

import at.fhtw.grantscout.core.ParseCallUseCase;
import at.fhtw.grantscout.core.events.CallScrapedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class CallScrapedEventListener implements ApplicationListener<CallScrapedEvent> {

    private final ParseCallUseCase parseCallUseCase;

    public CallScrapedEventListener(ParseCallUseCase parseCallUseCase) {
        this.parseCallUseCase = parseCallUseCase;
    }

    @Override
    public void onApplicationEvent(CallScrapedEvent event) {
        parseCallUseCase.parseCall(event.getScrapedCallId());
    }
}
