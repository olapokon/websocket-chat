package ak4ra.websocketchat.messages;

import java.time.LocalDateTime;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class MessageService {

    private static final String CHATROOM_DESTINATION_PREFIX = "/topic";

    private final Logger log = LoggerFactory.getLogger(MessageService.class);

    private final SimpMessagingTemplate template;
    private final ObjectMapper          objectMapper = new ObjectMapper();

    @Autowired
    public MessageService(SimpMessagingTemplate template) {
        this.template = template;
    }

    /**
     * Sends a STOMP message with the given body to the given destination.
     *
     * @param destination
     *         the destination to be passed to the message broker
     * @param headers
     *         the STOMP message's headers
     * @param message
     *         the body of the message
     */
    public void sendUserMessage(String destination,
                                Map<String, Object> headers,
                                String message) throws JsonProcessingException {
        log.info("-----------------------------------------------------------------");
        OAuth2AuthenticationToken oauth2Token = (OAuth2AuthenticationToken) headers.get("simpUser");
        Map<String, Object> userAttributes = oauth2Token.getPrincipal().getAttributes();
        String username = userAttributes.get("login").toString(); // github username
        String userId = userAttributes.get("id").toString(); // github id

        //        String s = headers
        //                .entrySet()
        //                .stream()
        //                .map(e -> e.getKey() + ":\n\t" + e.getValue() + "\n")
        //                .collect(Collectors.joining());
        log.info("username: {}, user id: {}", username, userId);
        log.info("message: {}", message);
        //        log.info("headers: {}", headers);
        log.info("destination: /ws-chat/{}", destination);
        ChatMessage body = new ChatMessage(ChatMessageType.USER_MESSAGE,
                                           username,
                                           message,
                                           LocalDateTime.now().toString());
        String messageJson = objectMapper.writeValueAsString(body);
        log.info("messageJson: {}", messageJson);
        log.info("-----------------------------------------------------------------");

        this.template.convertAndSend(destination, messageJson);
    }

    public void sendChatroomMessage(ChatroomEvent event,
                                    String chatroomId) throws JsonProcessingException {

        ChatMessage body = new ChatMessage(ChatMessageType.CHATROOM_MESSAGE,
                                           chatroomId,
                                           event.toString(),
                                           LocalDateTime.now().toString());
        String messageJson = objectMapper.writeValueAsString(body);
        String destination = CHATROOM_DESTINATION_PREFIX + "/" + chatroomId;
        this.template.convertAndSend(destination, messageJson);
    }
}
