package at.fhtw.grantscout.in.rest;

import at.fhtw.grantscout.core.ChatService;
import at.fhtw.grantscout.core.domain.data.chat.ChatRequest;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/chat")
public class ChatController {
    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, String> chat(@RequestBody ChatRequest req) {
        String reply = chatService.chat(req.history(), req.message());
        return Map.of("reply", reply);
    }
}
