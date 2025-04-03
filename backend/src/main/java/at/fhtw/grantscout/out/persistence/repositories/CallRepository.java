package at.fhtw.grantscout.out.persistence.repositories;

import at.fhtw.grantscout.out.persistence.entities.Call;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CallRepository extends JpaRepository<Call, Long> {

    Optional<Call> findByUrl(String url);

}
