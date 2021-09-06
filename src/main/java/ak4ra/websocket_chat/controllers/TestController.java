package ak4ra.websocket_chat.controllers;

import ak4ra.websocket_chat.websockethandlers.MainWebsocketHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class TestController {

	private SimpMessagingTemplate template;

	@Autowired
	public TestController(SimpMessagingTemplate template) {
		this.template = template;
	}

	private final Logger log = LoggerFactory.getLogger(TestController.class);

	@MessageMapping("/ws-test")
	public void handle(String t) {
		log.info("message received: " + t);
		//		return "[" + getTimestamp() + ": " + greeting;
		String r = "[" + t + "] received";
		this.template.convertAndSend("/queue", r);
	}
}
