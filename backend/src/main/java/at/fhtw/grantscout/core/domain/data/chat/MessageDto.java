package at.fhtw.grantscout.core.domain.data.chat;

import java.time.LocalDateTime;

public record MessageDto(
        String role,
        String content,
        LocalDateTime createdAt
) {}

