package at.fhtw.grantscout.core.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class CallScrapedEvent extends ApplicationEvent {

    private Long scrapedCallId;

    public CallScrapedEvent(Object source, Long scrapedCallId) {
        super(source);
        this.scrapedCallId = scrapedCallId;
    }
}
