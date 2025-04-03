package at.fhtw.grantscout.core.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Institute {
    FFG("FFG"),
    EU("EU"),
    ;

    private final String name;
}
