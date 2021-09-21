package ak4ra.websocketchat.chatroom;

import ak4ra.websocketchat.entities.Chatroom;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatroomRepository extends MongoRepository<Chatroom, String> {
}
