package olapokon.websocketchat.messages;

import olapokon.websocketchat.entities.User;
import olapokon.websocketchat.util.SimpMessageHeadersUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
public class MessageController {

    private final MessageService messageService;

    @Autowired
    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @MessageMapping("/ws-chat/{variableDestination}")
    public void handle(@DestinationVariable("variableDestination") String variableDestination,
                       Message<?> message,
                       String messageBody) {

        User u = SimpMessageHeadersUtil.extractUser(message);
        String destination = "/topic/" + variableDestination;
        messageService.sendUserMessage(destination, u.getUsername(), messageBody);
    }
}
