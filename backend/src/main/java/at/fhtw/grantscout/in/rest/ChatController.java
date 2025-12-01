package at.fhtw.grantscout.in.rest;

import at.fhtw.grantscout.core.ChatService;
import at.fhtw.grantscout.core.domain.data.chat.ChatRequest;
import at.fhtw.grantscout.core.domain.data.chat.ConversationDto;
import at.fhtw.grantscout.core.domain.data.chat.MessageDto;
import at.fhtw.grantscout.out.persistence.entities.ChatConversation;
import at.fhtw.grantscout.out.persistence.entities.ChatMessage;
import at.fhtw.grantscout.out.persistence.repositories.ChatConversationRepository;
import at.fhtw.grantscout.out.persistence.repositories.ChatMessageRepository;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/chat")
public class ChatController {
    private final ChatService chatService;
    private final ChatConversationRepository conversationRepository;
    private final ChatMessageRepository messageRepository;

    public ChatController(ChatService chatService,
                          ChatConversationRepository conversationRepository,
                          ChatMessageRepository messageRepository) {
        this.chatService = chatService;
        this.conversationRepository = conversationRepository;
        this.messageRepository = messageRepository;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, String> chat(@RequestBody ChatRequest req) {
        String reply = chatService.chat(req.conversationId(), req.message());
        return Map.of("reply", reply);
    }

    @PostMapping("/new")
    public Map<String, Long> createNewConversation() {
        Long conversationId = chatService.createNewConversation();
        return Map.of("conversationId", conversationId);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ConversationDto> listConversations() {
        List<ChatConversation> conversations = conversationRepository.findAllByOrderByUpdatedAtDesc();
        return conversations.stream()
                .map(c -> new ConversationDto(c.getId(), c.getUpdatedAt()))
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}/messages")
    public List<MessageDto> getConversationMessages(@PathVariable Long id) {
        List<ChatMessage> messages = messageRepository.findByConversationIdOrderByCreatedAtAsc(id);
        return messages.stream()
                .map(m -> new MessageDto(m.getRole(), m.getContent(), m.getCreatedAt()))
                .collect(Collectors.toList());
    }
}
