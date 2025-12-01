package at.fhtw.grantscout.core.domain.data.chat;

import java.time.LocalDateTime;

public record ConversationDto(
        Long id,
        LocalDateTime updatedAt
) {}

