package ak4ra.websocketchat.chatroom;

import java.util.Optional;

import ak4ra.websocketchat.entities.Chatroom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatroomRepository extends JpaRepository<Chatroom, Long> {

    Optional<Chatroom> findChatroomByName(String name);

    @Query(value = "SELECT c FROM Chatroom c JOIN FETCH c.authorizedUsers WHERE c.id = ?1")
    Optional<Chatroom> findChatroomByIdAndFetchAuthorizedUsers(Long id);

    @Query(value = "SELECT c FROM Chatroom c JOIN FETCH c.activeUsers WHERE c.id = ?1")
    Optional<Chatroom> findChatroomByIdAndFetchActiveUsers(Long id);
}
