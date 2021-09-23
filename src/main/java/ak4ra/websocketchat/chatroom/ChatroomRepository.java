package ak4ra.websocketchat.chatroom;

import java.util.Optional;

import ak4ra.websocketchat.entities.Chatroom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatroomRepository extends JpaRepository<Chatroom, Long> {

    Optional<Chatroom> findChatroomByName(String name);
}
