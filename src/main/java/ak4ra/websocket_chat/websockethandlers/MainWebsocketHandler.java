package ak4ra.websocket_chat.websockethandlers;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

@Component
public class MainWebsocketHandler implements WebSocketHandler {

	private final Logger log = LoggerFactory.getLogger(MainWebsocketHandler.class);

	private static Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

	@Override
	public void afterConnectionEstablished(WebSocketSession session) {
		log.info("new websocket connection: {}", session);
		sessions.put(session.getId(), session);

		log.info("NUMBER OF OPEN SESSIONS: {}", sessions.size());
	}

	@Override
	public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) {
		log.info("new message:\n\tsession: {}\n\tmessage: {}\n\tpayload: {}\n\thandshakeHeaders: {}" +
				 "\n\tattributes: {}\n\tprincipal: {}\n\tlocalAddress: {}\n\tremoteAddress: {}" +
				 "\n\tacceptedProtocol: {}\n\textensions: {}",
				 session.toString(),
				 message,
				 message.getPayload(),
				 session.getHandshakeHeaders(),
				 session.getAttributes(),
				 session.getPrincipal(),
				 session.getLocalAddress(),
				 session.getRemoteAddress(),
				 session.getAcceptedProtocol(),
				 session.getExtensions()
		);
		try {
			session.sendMessage(new TextMessage("message received"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void handleTransportError(WebSocketSession session, Throwable exception) {
		log.error("transport error for session: {}", session.toString(), exception);
	}

	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) {
		log.info("closed connection: {}", session.toString());
		sessions.remove(session.getId());
	}

	@Override
	public boolean supportsPartialMessages() {
		return false;
	}
}
