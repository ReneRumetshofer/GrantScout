package at.fhtw.grantscout.core;

import at.fhtw.grantscout.core.domain.data.chat.Message;
import com.openai.client.OpenAIClient;
import com.openai.models.ChatModel;
import com.openai.models.chat.completions.ChatCompletion;
import com.openai.models.chat.completions.ChatCompletionCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatService {
    private final OpenAIClient client;
    private final ChatModel model;


    public ChatService(OpenAIClient client, @Value("${app.openai.model}") String modelName) {
        this.client = client;
        this.model = ChatModel.of(modelName);
    }


    public String chat(List<Message> history, String userMessage) {
        ChatCompletionCreateParams.Builder builder = ChatCompletionCreateParams.builder()
                .model(model)
                .temperature(0.2)
                .addSystemMessage("Du bist ein Assistent für Forschungsförderungsanträge." +
                        "Stelle präzise Rückfragen, sammle fehlende Pflichtangaben " +
                        "(Programm, Einreichfrist, Budget, Konsortialpartner, Ziele, Impact, Workpackages, Risiken) " +
                        "und antworte strukturiert.");


        if (history != null) {
            for (Message m : history) {
                if (m.role().equals("assistant")) {
                    builder.addAssistantMessage(m.content());
                } else {
                    builder.addUserMessage(m.content());
                }
            }
        }


        builder.addUserMessage(userMessage);


        ChatCompletion completion = client.chat().completions().create(builder.build());
        return completion.choices().isEmpty()
                ? "(keine Antwort erhalten)"
                : completion.choices().getFirst().message().content().orElse("");
    }
}
