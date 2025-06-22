package at.fhtw.grantscout.out.persistence.repositories;

import at.fhtw.grantscout.core.domain.enums.CallStatus;
import at.fhtw.grantscout.out.persistence.entities.ScrapedCall;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScrapedCallRepository extends JpaRepository<ScrapedCall, Long> {

    List<ScrapedCall> findAllByCall_Status(CallStatus callStatus);
    
}
