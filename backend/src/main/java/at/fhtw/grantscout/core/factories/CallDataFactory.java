package at.fhtw.grantscout.core.factories;

import at.fhtw.grantscout.core.domain.data.CallData;
import at.fhtw.grantscout.out.persistence.entities.Call;
import org.springframework.stereotype.Component;

@Component
public class CallDataFactory {

    public CallData fromDbEntity(Call call) {
        return CallData.builder()
                .id(call.getId())
                .url(call.getUrl())
                .institute(call.getInstitute())
                .status(call.getStatus())
                .build();
    }

}
