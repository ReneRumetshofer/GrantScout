package at.fhtw.grantscout.core.factories;

import at.fhtw.grantscout.core.domain.data.ParsedCallData;
import at.fhtw.grantscout.core.domain.data.ParsingResult;
import at.fhtw.grantscout.out.persistence.entities.ParsedCall;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ParsedDataFactory {

    private Logger logger = LoggerFactory.getLogger(ParsedDataFactory.class);

    private final CallDataFactory callDataFactory;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public ParsedDataFactory(CallDataFactory callDataFactory) {
        this.callDataFactory = callDataFactory;
    }

    public ParsedCallData fromDbEntity(ParsedCall parsedCall) {
        try {
            return ParsedCallData.builder()
                    .call(callDataFactory.fromDbEntity(parsedCall.getCall()))
                    .parsedAt(parsedCall.getParsedAt())
                    .parsedData(objectMapper.readValue(parsedCall.getJsonData(), ParsingResult.class))
                    .build();
        } catch (JsonProcessingException e) {
            logger.error("Error reading parsing result from DB: {}", e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }
}
