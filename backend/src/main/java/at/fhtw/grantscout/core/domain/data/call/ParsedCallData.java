package at.fhtw.grantscout.core.domain.data.call;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ParsedCallData (
        CallData call,
        LocalDateTime parsedAt,
        ParsingResult parsedData
) {
}
