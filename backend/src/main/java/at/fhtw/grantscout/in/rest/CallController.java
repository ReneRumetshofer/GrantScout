package at.fhtw.grantscout.in.rest;

import at.fhtw.grantscout.core.FindCallsUseCase;
import at.fhtw.grantscout.core.domain.enums.CallStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/calls")
public class CallController {

    private final FindCallsUseCase findCallsUseCase;

    public CallController(FindCallsUseCase findCallsUseCase) {
        this.findCallsUseCase = findCallsUseCase;
    }

    @GetMapping
    public ResponseEntity<Object> getCalls(@RequestParam("type") CallStatus status) {
        return switch (status) {
            case CallStatus.SCRAPED -> ResponseEntity.ok(findCallsUseCase.findAllScrapedCalls());
            case CallStatus.PARSED -> ResponseEntity.ok(findCallsUseCase.findAllParsedCalls());
            default -> ResponseEntity.notFound().build();
        };
    }

}
