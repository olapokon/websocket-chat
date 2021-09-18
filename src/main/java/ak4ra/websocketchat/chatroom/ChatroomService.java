package ak4ra.websocketchat.chatroom;

import java.util.Set;

import org.springframework.stereotype.Service;

@Service
public class ChatroomService {

    /**
     * Chatrooms available by default.
     */
    // TODO: move elsewhere
    private static final Chatroom[] DEFAULT_CHATROOMS = {
            new Chatroom(1L, "default chatroom 1", "/default-1", Set.of(45451374L)),
            new Chatroom(2L, "default chatroom 2", "/default-2", Set.of()),
            new Chatroom(3L, "default chatroom 3", "/default-3", Set.of()),
            new Chatroom(4L, "default chatroom 4", "/default-4", Set.of()),
            };

    public Chatroom[] getChatrooms() {
        return DEFAULT_CHATROOMS;
    }
}
