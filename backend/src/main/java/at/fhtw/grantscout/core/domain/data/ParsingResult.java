package at.fhtw.grantscout.core.domain.data;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public record ParsingResult(
        String name,
        String topic,
        String shortDescription,
        String longDescription,
        String organization,
        String grantCallSum,
        ApplicationDeadlines applicationDeadlines,
        String regions,
        String targetGroup,
        String website,
        List<Contact> contact,
        OptionalData optional
) {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record ApplicationDeadlines(
            String from,
            String to
    ) {}

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record Contact(
            String contactPerson,
            String email,
            String telephone
    ) {}

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record OptionalData(
            String status,
            String frequency,
            String preconditions,
            String neededDocuments,
            String evaluationCriteria,
            String applicationPlatform,
            String costsOfApplication,
            String grantType,
            String grantDuration,
            String grantQuota,
            String possibleActivities
    ) {}
}