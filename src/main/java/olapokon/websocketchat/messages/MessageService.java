package olapokon.websocketchat.messages;

import java.time.LocalDateTime;
import java.util.List;

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
     * Sends a STOMP message with the given body to the given destination.
     *
     * @param destination
     *         the message broker destination
     * @param username
     *         the sender's username
     * @param message
     *         the body of the message
     */
    public void sendUserMessage(String destination,
                                String username,
                                String message) throws JsonProcessingException {
        log.trace("-----------------------------------------------------------------"); // TODO: remove
        log.trace("username: {}", username);
        log.trace("message: {}", message);
        log.trace("destination: {}", destination);
        ChatMessage body = new ChatMessage(ChatMessageType.USER_MESSAGE,
                                           username,
                                           message,
                                           LocalDateTime.now().toString());
        String messageJson = objectMapper.writeValueAsString(body);
        log.trace("messageJson: {}", messageJson);
        log.trace("-----------------------------------------------------------------");

        this.template.convertAndSend(destination, messageJson);
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
        log.trace("-----------------------------------------------------------------"); // TODO: remove
        log.trace("destination: {}", destination);
        ChatMessage body = new ChatMessage(ChatMessageType.USER_LIST_UPDATE,
                                           "",
                                           userListJson,
                                           LocalDateTime.now().toString());
        String messageJson = objectMapper.writeValueAsString(body);
        log.trace("messageJson: {}", messageJson);
        log.trace("-----------------------------------------------------------------");

        this.template.convertAndSend(destination, messageJson);
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
    public void sendUserJoinedMessage(String destination, String username)
    throws JsonProcessingException {
        sendChatroomMessage(destination, username, ChatroomEvent.USER_JOINED);
    }

    /**
     * Notifies a chatroom that a user has disconnected.
     *
     * @param destination
     *         the chatroom's message broker destination
     * @param username
     *         the user's username
     *
     * @throws JsonProcessingException
     */
    public void sendUserLeftMessage(String destination, String username) throws JsonProcessingException {
        sendChatroomMessage(destination, username, ChatroomEvent.USER_LEFT);
    }

    /**
     * Sends a STOMP message to a chatroom.
     *
     * @param destination
     *         the chatroom's message broker destination
     * @param username
     *         the username of the user on whose behalf the message is being sent.
     *
     * @throws JsonProcessingException
     */
    public void sendChatroomMessage(String destination,
                                    String username,
                                    ChatroomEvent event) throws JsonProcessingException {
        ChatMessage body = new ChatMessage(ChatMessageType.CHATROOM_MESSAGE,
                                           username,
                                           event.toString(),
                                           LocalDateTime.now().toString());
        String messageJson = objectMapper.writeValueAsString(body);

        this.template.convertAndSend(destination, messageJson);
    }
}
