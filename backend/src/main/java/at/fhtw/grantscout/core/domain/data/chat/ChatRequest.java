package at.fhtw.grantscout.core.domain.data.chat;

public record ChatRequest(
        Long conversationId,
        String message
) {}
