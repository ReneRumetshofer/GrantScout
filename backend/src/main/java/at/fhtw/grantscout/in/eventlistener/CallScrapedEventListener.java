package at.fhtw.grantscout.in.eventlistener;

import at.fhtw.grantscout.core.events.CallScrapedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class CallScrapedEventListener implements ApplicationListener<CallScrapedEvent> {

    @Override
    public void onApplicationEvent(CallScrapedEvent event) {

    }
}
