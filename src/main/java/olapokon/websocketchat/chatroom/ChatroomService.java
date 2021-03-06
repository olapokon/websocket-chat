package olapokon.websocketchat.chatroom;

import java.util.List;

import olapokon.websocketchat.entities.User;
import olapokon.websocketchat.presence.UserPresenceTracker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChatroomService {

    private final UserPresenceTracker userPresenceTracker;

    @Autowired
    public ChatroomService(UserPresenceTracker userPresenceTracker) {
        this.userPresenceTracker = userPresenceTracker;
    }

    // TODO: implement if needed or remove this method
    public List<Object> findAllChatrooms() {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns the usernames of the users currently connected to the chatroom.
     */
    public List<String> getActiveUsersList(String destination) {
        return userPresenceTracker.getUserList(destination)
                                  .stream()
                                  .map(User::getUsername)
                                  .toList();
    }
}
