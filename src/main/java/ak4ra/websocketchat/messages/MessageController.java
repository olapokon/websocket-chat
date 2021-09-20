package ak4ra.websocketchat.messages;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;

@Controller
public class MessageController {

    private final SimpMessagingTemplate template;

    @Autowired
    public MessageController(SimpMessagingTemplate template) {
        this.template = template;
    }

    private final Logger log = LoggerFactory.getLogger(MessageController.class);

    @MessageMapping("/ws-chat/{variableDestination}")
    public void handle(@DestinationVariable("variableDestination") String variableDestination,
                       @Headers Map<String, Object> headers,
                       String message) throws JsonProcessingException {
        log.info("-----------------------------------------------------------------");
        OAuth2AuthenticationToken oauth2Token = (OAuth2AuthenticationToken) headers.get("simpUser");
        Map<String, Object> userAttributes = oauth2Token.getPrincipal().getAttributes();
        String username = userAttributes.get("login").toString(); // github username
        String userId = userAttributes.get("id").toString(); // github id

        String s = headers
                .entrySet()
                .stream()
                .map(e -> e.getKey() + ":\n\t" + e.getValue() + "\n")
                .collect(Collectors.joining());
        log.info("username: {}, user id: {}", username, userId);
        log.info("message: {}", message);
        log.info("destination: /ws-chat/{}", variableDestination);

        ObjectMapper objectMapper = new ObjectMapper();
        MessageBody body = new MessageBody(username, message, LocalDateTime.now().toString());
        String messageJson = objectMapper.writeValueAsString(body);
        log.info("messageJson: {}", messageJson);
        log.info("-----------------------------------------------------------------");

        String destination = "/topic/" + variableDestination;
        this.template.convertAndSend(destination, messageJson);
    }
}
