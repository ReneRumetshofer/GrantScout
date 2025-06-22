package at.fhtw.grantscout.core.domain.data;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ScrapedCallData(
        CallData call,
        LocalDateTime scrapedAt,
        String content
) {
}
