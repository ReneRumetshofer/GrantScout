package org.example.parsing;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.service.OpenAiService;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Parser {
    private static final List<ChatMessage> PROMPTSETUP = new ArrayList<>(Arrays.asList(
        new ChatMessage("system", "You are a helpful assistant for standardizing grant information."),
        new ChatMessage("user",
                """
                Bitte standardisiere die folgenden Förderinformationen in reinem JSON-Format, ohne Formatierungselemente wie Markdown oder zusätzliche Kommentare.
                Die Optional- sowie Kontakt-Felder sollen nur befüllt werden, wenn diese auch eindeutig festgestellt werden können.\s
                Erzeuge keine Werte mit null. Falls kein Datum ermittelt werden kann, schreibe "unbekannt", oder "laufend".
                Falls ein anderes Feld außer Datum nicht ermittelt werden kann, lasse es komplett weg.

                Beispiel:
                {{
                    "Name": "Beispiel Förderprogramm",
                    "Themenbereich": "Beispiel Kategorie:
                        Digitalisierung & Breitband
                        Energiewende
                        Europa & Internationales
                        Gesellschaft & Sicherheit
                        Holzforschung & -wissenstransfer
                        Innovative & wettbewerbsfähige Unternehmen
                        Klimaneutrale Stadt
                        Kooperation & Forschungsinfrastruktur
                        Kreislaufwirtschaft
                        Lebenswissenschaften & Gesundheit
                        Menschen, Qualifikation & Gender
                        Mobilitätswende
                        Produktion & Material
                        Quantenforschung & -technologie
                        Weltraum & Luftfahrt,
                        etc.",
                    "Kurzbeschreibung": "Ausschreibungszweck, Beschreibung der Ziele, maximal 100 Zeichen, obligatorisch",
                    "Langbeschreibung": "Ausführliche Beschreibung der Ausschreibung, maximal 500 Zeichen",
                    "Organisation": "Beispiel Stiftung oder Institut",
                    "Förderbetrag": "Bis zu 50.000 Euro, Ausnahmen bis 20.000 Euro",
                    "Bewerbungsfrist": {{
                        "Von": "TT.MM.JJJJ HH:MM"
                        "Bis": "TT.MM.JJJJ HH:MM"
                    }},
                    "Regionen": ["Deutschland", "Europa", etc.],
                    "Zielgruppe": ["Startups", "Non-Profits", "Forschung", "KMU", "öffentliche Infrastruktur", etc.],
                    "Webseite": "https://beispiel.foerderung.de",
                    "Kontakt": [
                        {{
                            "Ansprechpartner": "Tom Turbo",
                            "E-Mail": "tom.turbo@gmail.com",
                            "Telefon": "+43 1 234 56 789"
                        }}
                    ],
                    "Optional": {{
                        "Status": "Aktiv, geschlossen, verlängert, etc.",
                        "Häufigkeit": "Einmalig, jährlich, laufend, etc.",
                        "Voraussetzungen": "Mindestalter, Umsatzgrenzen, etc.",
                        "Notwendige Dokumente": "Finanzberichte, Lebenslauf, etc.",
                        "Bewertungskriterien": "Innovation, soziale Wirkung, Nachhaltigkeit, etc.",
                        "Bewerbungsplattform": "Spezielles Portal zum Einreichen, wenn möglich mit Namen",
                        "Kosten": "Bewerbungsgebühren",
                        "Fördertyp": "Zuschuss, Darlehen, Beteiligungskapital, Sachleistung, etc.",
                        "Förderdauer": "Zeitraum",
                        "Förderquote": "Prozentsatz der geförderten Kosten",
                        "Mögliche Aktivitäten": "Welche Maßnahmen werden gefördert (Personal, Material, Marketing, etc.)"
                    }}
                }}

                Der Text steht in der nächsten Nachricht.
            """
    )));

    public void parse(String text) {
        System.out.println("Parsing...");
        String apiKey = System.getenv("OPENAI_API_KEY");
        System.out.println("API Key: " + apiKey.substring(0, 7) + "*".repeat(apiKey.length()-7));

        OpenAiService service = new OpenAiService(apiKey, Duration.ofSeconds(30));

        List<ChatMessage> PROMPT = new ArrayList<>(PROMPTSETUP);
        PROMPT.add(new ChatMessage("user", text));

        ChatCompletionRequest request = ChatCompletionRequest.builder()
                .model("gpt-4")
                .messages(PROMPT)
                .build();

        service.createChatCompletion(request)
                .getChoices()
                .stream()
                .map(choice -> {
                    System.out.println(choice.getMessage().getContent());
                    try {
                        return new ObjectMapper().readValue(choice.getMessage().getContent(), ParsedCall.class);
                    } catch (Exception e) {
                        throw new RuntimeException("Fehler beim Parsen von JSON", e);
                    }
                })
                .forEach(System.out::println);


        service.shutdownExecutor();
        System.out.println("Parsing done.");
    }
}
