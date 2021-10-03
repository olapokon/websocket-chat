package olapokon.websocketchat.chatroom;

import java.util.Optional;

import olapokon.websocketchat.entities.Chatroom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatroomRepository extends JpaRepository<Chatroom, Long> {

    Optional<Chatroom> findChatroomByName(String name);

    @Query(value = "SELECT c FROM Chatroom c LEFT JOIN FETCH c.authorizedUsers WHERE c.id = :id")
    Optional<Chatroom> getChatroomByIdAndFetchAuthorizedUsers(@Param("id") Long id);
}
