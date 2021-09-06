package ak4ra.websocket_chat.config;

import ak4ra.websocket_chat.websockethandlers.MainWebsocketHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

//@Configuration
//@EnableWebSocket
//public class WebSocketConfig implements WebSocketConfigurer {
//
//	@Override
//	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
//		registry.addHandler(new MainWebsocketHandler(), "/")
//				.setAllowedOrigins("*"); // TODO: only for development
//	}
//}

// TODO: https://docs.spring.io/spring-framework/docs/current/reference/html/web.html#websocket
// TODO: https://docs.spring.io/spring-framework/docs/4.3.x/spring-framework-reference/html/websocket.html#websocket-stomp-enable
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		// the HTTP URL for the endpoint to which a WebSocket client will need to connect to
		// for the WebSocket handshake
		registry.addEndpoint("/ws")
				.setAllowedOrigins("*"); // TODO: only for development
	}

	@Override
	public void configureMessageBroker(MessageBrokerRegistry config) {
		// STOMP messages whose destination header begins with these prefixes
		// are routed to @MessageMapping methods in @Controller classes,
		// before they're sent to the message broker?
		config.setApplicationDestinationPrefixes("/app");

		// STOMP messages whose destination header begins with these prefixes
		// are routed directly to the message broker.
		config.enableSimpleBroker("/topic", "/queue");
	}
}
