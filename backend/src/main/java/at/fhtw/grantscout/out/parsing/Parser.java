package at.fhtw.grantscout.out.parsing;

import at.fhtw.grantscout.core.domain.data.call.ParsingResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.models.ChatModel;
import com.openai.models.chat.completions.ChatCompletion;
import com.openai.models.chat.completions.ChatCompletionCreateParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Parser {

    private final Logger logger = LoggerFactory.getLogger(Parser.class);

    @Value("${app.openai.apiKey}")
    private String openApiKey;

    private static final String systemPrompt = "You are a helpful assistant for standardizing information in grant calls published by grant givers. Your job is to convert raw information into standardized JSON.";
    private static final String parsingPrompt = """
                                Please use the following standardized JSON structure to represent funding program information. All fields must be preserved, even if the value is null. Do not invent or assume information. Only fill in optional and contact fields if they are clearly identifiable. All values may be provided as strings, even for numerical values. Ensure the structure exactly matches the schema below:
           
                                {
                                  "name": "Name of the funding program",
                                  "topic": "Primary topic or category, e.g. Digitalisierung & Breitband, Energiewende, etc.",
                                  "shortDescription": "Brief purpose of the funding call, max. 100 characters (mandatory)",
                                  "longDescription": "Detailed description of the funding call, max. 500 characters",
                                  "organization": "Name of the responsible organization or institution",
                                  "grantCallSum": "Total available funding or typical funding amount per project (e.g. 'Up to €50,000')",
                                  "applicationDeadlines": {
                                    "from": "DD.MM.YYYY HH:MM",
                                    "to": "DD.MM.YYYY HH:MM"
                                  },
                                  "regions": "List of eligible geographic regions (e.g. Deutschland, Europa, etc.)",
                                  "targetGroup": "List of eligible applicants (e.g. Startups, Non-Profits, KMUs, etc.)",
                                  "website": "Official website URL for the funding program",
                                  "contact": [
                                    {
                                      "contactPerson": "Full name of a contact person",
                                      "email": "Valid email address",
                                      "telephone": "Phone number including international dialing code"
                                    }
                                  ],
                                  "optional": {
                                    "status": "Current status of the call (e.g. Aktiv, geschlossen, verlängert, etc.)",
                                    "frequency": "Frequency of the call (e.g. einmalig, jährlich, laufend)",
                                    "preconditions": "Specific eligibility criteria (e.g. age limits, revenue thresholds)",
                                    "neededDocuments": "Required documents for application (e.g. financial reports, CVs)",
                                    "evaluationCriteria": "Key criteria used to evaluate applications (e.g. innovation, social impact)",
                                    "applicationPlatform": "Name and/or URL of the submission platform",
                                    "costsOfApplication": "Any fees or costs for submitting an application",
                                    "grantType": "Type of support (e.g. Zuschuss, Darlehen, Beteiligungskapital, Sachleistung)",
                                    "grantDuration": "Duration of the funding support (e.g. in months or years)",
                                    "grantQuota": "Funding rate or percentage of eligible costs covered",
                                    "possibleActivities": "Types of activities or expenditures eligible for funding (e.g. personnel, materials, marketing)"
                                  }
                                }
                                
                                Example:
                                {
                                  "name": "European Network of Factcheckers",
                                  "topic": "Accelerating the Best Use of Technologies",
                                  "shortDescription": "Support activities aiming at increasing fact-checking capacity and coverage across the EU.",
                                  "longDescription": "The EU supports the capacity of a multidisciplinary community to understand, monitor and counter disinformation. This topic aims to maintain and further develop a platform supporting the operations of the EDMO, deepening the language coverage and operational capacity of fact-checking in Europe. The scope of this work is to strengthen the capacity of the European fact-checking community and make fact-checking accessible in all languages.",
                                  "organization": "European Commission",
                                  "grantCallSum": "Up to €5,000,000",
                                  "applicationDeadlines": {
                                    "from": "15.04.2025 00:00",
                                    "to": "02.09.2025 17:00"
                                  },
                                  "regions": "EU, Candidate and accession countries, associated to the Programme",
                                  "targetGroup": "Fact-checking organizations, Researchers, Media literacy practitioners",
                                  "website": "European Commission - Funding and Tenders Portal",
                                  "contact": [
                                    {
                                      "contactPerson": null,
                                      "email": null,
                                      "telephone": null
                                    }
                                  ],
                                  "optional": {
                                    "status": "Open For Submission",
                                    "frequency": "single-stage",
                                    "preconditions": "Details described in sections 5, 6, 7, 8, 9, 10 of the call document",
                                    "neededDocuments": "Application form templates available in the Submission System",
                                    "evaluationCriteria": "Details in section 5b of the call document",
                                    "applicationPlatform": "Funding & Tenders Portal",
                                    "costsOfApplication": null,
                                    "grantType": "DIGITAL-GFS DIGITAL Grants for Financial Support",
                                    "grantDuration": null,
                                    "grantQuota": null,
                                    "possibleActivities": "Collaboration activities, peer-to-peer support, technical infrastructure/tools supporting the activities of fact-checkers, targeted trainings for fact-checkers and media professionals"
                                  }
                                }
            
                                The text to parse can be found in the next message. Please respond exactly according to the instructions and do not simply reuse the values of the example. Stick to the instructions religiously.
                            """;

    public ParsingResult parse(String payload) {
        OpenAIClient client = OpenAIOkHttpClient.builder()
                .apiKey(openApiKey)
                .build();

        ChatCompletionCreateParams params = ChatCompletionCreateParams.builder()
                .addSystemMessage(systemPrompt)
                .addUserMessage(parsingPrompt)
                .addUserMessage(payload)
                .model(ChatModel.GPT_4)
                .build();
        ChatCompletion chatCompletion = client.chat().completions().create(params);
        if (chatCompletion.choices().isEmpty()) {
            throw new RuntimeException("Parsing scraped JSON failed, no choices returned from OpenAI");
        }

        ChatCompletion.Choice choice = chatCompletion.choices().getFirst();
        try {
            String result = choice.message().content().orElseThrow();
            logger.debug("Parsing OpenAI JSON result: {}", result);
            return new ObjectMapper().readValue(result, ParsingResult.class);
        } catch (Exception e) {
            throw new RuntimeException("An error occurred while parsing the JSON response from OpenAI", e);
        }
    }
}
