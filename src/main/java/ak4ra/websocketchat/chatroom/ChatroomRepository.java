package ak4ra.websocketchat.chatroom;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

@Component
public interface ChatroomRepository {

    Optional<Chatroom> getById(Long id);

    List<Chatroom> findAll();
}
