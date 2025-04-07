package at.fhtw.grantscout.core.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;

@Getter
@AllArgsConstructor
public enum Institute {
    FFG("FFG", "https://www.ffg.at/foerderungen?status[3]=3&status[1]=1&status[2]=2"),
    EU("EU", "https://ec.europa.eu/info/funding-tenders/opportunities/portal/screen/opportunities/calls-for-proposals?pageSize=100"),
    ;

    private final String name;
    private final String baseUrl;
}
