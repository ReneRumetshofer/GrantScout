package at.fhtw.grantscout.out.persistence.repositories;

import at.fhtw.grantscout.out.persistence.entities.ChatConversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatConversationRepository extends JpaRepository<ChatConversation, Long> {
    List<ChatConversation> findAllByOrderByUpdatedAtDesc();
}

