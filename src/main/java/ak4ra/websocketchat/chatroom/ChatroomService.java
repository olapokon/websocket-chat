package ak4ra.websocketchat.chatroom;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChatroomService {

    private final ChatroomRepository chatroomRepository;

    @Autowired
    public ChatroomService(ChatroomRepository chatroomRepository) {
        this.chatroomRepository = chatroomRepository;
    }

    public List<Chatroom> getChatrooms() {
        return chatroomRepository.findAll();
    }
}
