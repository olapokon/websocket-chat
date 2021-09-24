package ak4ra.websocketchat.messages;

import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Controller;

@Controller
public class MessageController {

    // TODO: move into constructor, or elsewhere
    @Autowired
    private SimpUserRegistry simpUserRegistry; // TODO: only local count, need a different registry if other nodes

    private final Logger log = LoggerFactory.getLogger(MessageController.class);

    private final MessageService messageService;

    @Autowired
    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @MessageMapping("/ws-chat/{variableDestination}")
    public void handle(@DestinationVariable("variableDestination") String variableDestination,
                       @Headers Map<String, Object> headers,
                       String message) throws JsonProcessingException {

        String destination = "/topic/" + variableDestination;
        log.info("user count: {}", simpUserRegistry.getUserCount());
        log.info("users: {}", simpUserRegistry.getUsers());
        messageService.sendUserMessage(destination, headers, message);
    }
}
