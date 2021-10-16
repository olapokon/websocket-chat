package olapokon.websocketchat.messages;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import olapokon.websocketchat.chatroom.ChatroomService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Service;

@Service
public class MessageService {

    // TODO: move into constructor, or elsewhere
    // TODO: probably remove completely
    @Autowired
    private SimpUserRegistry simpUserRegistry; // TODO: only local count, need a different registry if other nodes

    private final Logger log = LoggerFactory.getLogger(MessageService.class);

    private final SimpMessagingTemplate template;
    private final ChatroomService       chatroomService;
    private final ObjectMapper          objectMapper = new ObjectMapper();

    @Autowired
    public MessageService(SimpMessagingTemplate template, ChatroomService chatroomService) {
        this.template = template;
        this.chatroomService = chatroomService;
    }

    /**
     * Sends a user message coming from a chatroom to the given broker destination.
     *
     * @param destination
     *         the message broker destination
     * @param username
     *         the user's username
     * @param message
     *         the body of the message
     */
    public void sendUserMessage(String destination,
                                String username,
                                String message) {
        Map<String, Object> headers = new CustomStompHeaders()
                .addMessageType(ChatMessageType.USER_MESSAGE, username)
                .addTimestamp(ZonedDateTime.now().toString())
                .build();
        log.trace("-----------------------------------------------------------------"); // TODO: remove
        log.trace("SENDING STOMP FRAME:");
        log.trace("headers: {}", headers);
        log.trace("username: {}", username);
        log.trace("message: {}", message);
        log.trace("destination: {}", destination);
        log.trace("-----------------------------------------------------------------");

        this.template.convertAndSend(destination, message, headers);
    }

    /**
     * Sends the list of the usernames of the users currently connected to a chatroom, so it can be updated in the UI.
     *
     * @param destination
     *         the chatroom's message broker destination
     *
     * @throws JsonProcessingException
     */
    public void sendUserListUpdate(String destination) throws JsonProcessingException {
        List<String> userList = chatroomService.getActiveUsersList(destination);
        String userListJson = objectMapper.writeValueAsString(userList);
        Map<String, Object> headers = new CustomStompHeaders()
                .addMessageType(ChatMessageType.USER_LIST_UPDATE, userListJson)
                .addTimestamp(ZonedDateTime.now().toString())
                .build();
        log.trace("-----------------------------------------------------------------"); // TODO: remove
        log.trace("SENDING STOMP FRAME:");
        log.trace("headers: {}", headers);
        log.trace("destination: {}", destination);
        log.trace("-----------------------------------------------------------------");

        this.template.convertAndSend(destination, "", headers);
    }

    /**
     * Notifies a chatroom that a user has connected.
     *
     * @param destination
     *         the chatroom's message broker destination
     * @param username
     *         the user's username
     *
     * @throws JsonProcessingException
     */
    public void sendUserJoinedMessage(String destination, String username) {
        Map<String, Object> headers = new CustomStompHeaders()
                .addMessageType(ChatMessageType.USER_JOINED, username)
                .addTimestamp(ZonedDateTime.now().toString())
                .build();
        log.trace("-----------------------------------------------------------------"); // TODO: remove
        log.trace("SENDING STOMP FRAME:");
        log.trace("headers: {}", headers);
        log.trace("destination: {}", destination);
        log.trace("-----------------------------------------------------------------");

        this.template.convertAndSend(destination, "", headers);
    }

    /**
     * Notifies a chatroom that a user has disconnected.
     *
     * @param destination
     *         the chatroom's message broker destination
     * @param username
     *         the user's username
     */
    public void sendUserLeftMessage(String destination, String username) {
        Map<String, Object> headers = new CustomStompHeaders()
                .addMessageType(ChatMessageType.USER_LEFT, username)
                .addTimestamp(ZonedDateTime.now().toString())
                .build();
        log.trace("-----------------------------------------------------------------"); // TODO: remove
        log.trace("SENDING STOMP FRAME:");
        log.trace("headers: {}", headers);
        log.trace("destination: {}", destination);
        log.trace("-----------------------------------------------------------------");

        this.template.convertAndSend(destination, "", headers);
    }
}
