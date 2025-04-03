package at.fhtw.grantscout.core;

import at.fhtw.grantscout.core.domain.enums.CallStatus;
import at.fhtw.grantscout.core.domain.enums.Institute;
import at.fhtw.grantscout.core.ports.out.ForFFGCallSearch;
import at.fhtw.grantscout.out.persistence.entities.Call;
import at.fhtw.grantscout.out.persistence.repositories.CallRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import java.util.List;

@Component
public class CallSearchUseCase {

    @Value("${scraping.ffg_base_url}")
    private String ffgBaseUrl;

    private final ForFFGCallSearch forFFGCallSearch;
    private final CallRepository callRepository;

    private Logger logger = LoggerFactory.getLogger(CallSearchUseCase.class);

    public CallSearchUseCase(ForFFGCallSearch forFFGCallSearch, CallRepository callRepository) {
        this.forFFGCallSearch = forFFGCallSearch;
        this.callRepository = callRepository;
    }

    public void conductSearch() {
        logger.debug("Starting search for FFG");
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        List<String> ffgUrls = forFFGCallSearch.search(ffgBaseUrl);
        callRepository.findAll()
                .forEach(call -> ffgUrls.remove(call.getUrl()));
        logger.debug("Found {} new calls", ffgUrls.size());
        for(String url : ffgUrls) {
            Call newCall = Call.builder()
                    .status(CallStatus.FOUND)
                    .url(url)
                    .institute(Institute.FFG)
                    .build();
            callRepository.save(newCall);
        }
        logger.debug("Finished FFG search in {} ms", stopWatch.getTotalTimeMillis());
    }

}
