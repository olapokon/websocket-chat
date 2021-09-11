package ak4ra.websocketchat.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class TestController {

    private final SimpMessagingTemplate template;

    @Autowired
    public TestController(SimpMessagingTemplate template) {
        this.template = template;
    }

    private final Logger log = LoggerFactory.getLogger(TestController.class);

    @MessageMapping("/ws-test")
    // @SendTo("/queue")
    public void handle(String t) {
        log.info("message received: " + t);
        String r = "\"" + t + "\" received";
        this.template.convertAndSend("/topic/all", r);
        //  return r;
    }
}
