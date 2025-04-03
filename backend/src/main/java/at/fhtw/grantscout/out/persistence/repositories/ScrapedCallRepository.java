package at.fhtw.grantscout.out.persistence.repositories;

import at.fhtw.grantscout.out.persistence.entities.ScrapedCall;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScrapedCallRepository extends JpaRepository<ScrapedCall, Long> {
}
