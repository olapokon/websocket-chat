package olapokon.websocketchat.user;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import olapokon.websocketchat.entities.User;
import olapokon.websocketchat.messages.MessageService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private static final String USER_NOT_FOUND = "User not found.";

    private final MessageService messageService;

    @Autowired
    public UserService(MessageService messageService) {
        this.messageService = messageService;
    }

    /**
     * Handles the subscription of a user to a chatroom.
     *
     * @param user
     *         the user that is connecting to the chatroom
     * @param destination
     *         the chatroom's destination in the message broker
     *
     * @throws JsonProcessingException
     */
    public void userJoinChatroom(User user, String destination) throws JsonProcessingException {
        messageService.sendUserJoinedMessage(destination, user.getUsername());
        messageService.sendUserListUpdate(destination);
    }

    /**
     * Handles a user's disconnection from a chatroom.
     *
     * @param user
     *         the user that is disconnecting
     * @param destination
     *         the chatroom's destination in the message broker
     *
     * @throws JsonProcessingException
     */
    public void userLeaveChatroom(User user, String destination) throws JsonProcessingException {
        messageService.sendUserLeftMessage(destination, user.getUsername());
        messageService.sendUserListUpdate(destination);
    }
}
