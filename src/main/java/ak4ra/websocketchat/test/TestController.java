package ak4ra.websocketchat.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

@Controller
public class TestController {

    private final SimpMessagingTemplate template;

    @Autowired
    public TestController(SimpMessagingTemplate template) {
        this.template = template;
    }

    private final Logger log = LoggerFactory.getLogger(TestController.class);

    // TODO: get socket session id
    // https://stackoverflow.com/questions/42243543/how-to-get-session-id-in-spring-websocketstompclient
    @MessageMapping("/ws-chat/{variableDestination}")
    public void handle(@DestinationVariable("variableDestination") String variableDestination,
                       String message) {
        var context = SecurityContextHolder.getContext();
        log.info("security context: " + context);
        log.info("message: " + message
                 + ", destination: /ws-chat/" + variableDestination);
        String r = "\"" + message + "\" received";
        String destination = "/topic/" + variableDestination;
        this.template.convertAndSend(destination, r);
    }
}
