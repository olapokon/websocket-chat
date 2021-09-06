package ak4ra.websocket_chat.controllers;

import ak4ra.websocket_chat.websockethandlers.MainWebsocketHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
public class TestController {

	private final Logger log = LoggerFactory.getLogger(TestController.class);

	@MessageMapping("/ws-test")
	public String handle(String t) {
		log.info("message received: " + t);
		//		return "[" + getTimestamp() + ": " + greeting;
		return "[" + t + "] received";
	}
}
