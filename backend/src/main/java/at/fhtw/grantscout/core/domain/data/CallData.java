package at.fhtw.grantscout.core.domain.data;

import at.fhtw.grantscout.core.domain.enums.CallStatus;
import at.fhtw.grantscout.core.domain.enums.Institute;
import lombok.Builder;

@Builder
public record CallData(
        Long id,
        String url,
        Institute institute,
        CallStatus status
) {
}
