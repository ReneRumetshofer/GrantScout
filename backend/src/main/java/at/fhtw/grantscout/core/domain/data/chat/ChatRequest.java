package at.fhtw.grantscout.core.domain.data.chat;

import java.util.List;

public record ChatRequest(
        String message,
        List<Message> history
) {}
