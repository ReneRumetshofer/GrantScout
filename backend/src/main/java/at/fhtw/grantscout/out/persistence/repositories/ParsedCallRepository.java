package at.fhtw.grantscout.out.persistence.repositories;

import at.fhtw.grantscout.core.domain.enums.CallStatus;
import at.fhtw.grantscout.out.persistence.entities.ParsedCall;
import at.fhtw.grantscout.out.persistence.entities.ScrapedCall;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ParsedCallRepository extends JpaRepository<ParsedCall, Long> {

    List<ParsedCall> findAllByCall_Status(CallStatus callStatus);
    
}
