package ak4ra.websocketchat.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

// TODO: https://docs.spring.io/spring-framework/docs/current/reference/html/web.html#websocket
//
// TODO: WS sessions: https://docs.spring.io/spring-session/docs/current/reference/html5/guides/boot-websocket.html
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketMessageConfig implements WebSocketMessageBrokerConfigurer {
    @Value("${spring.rabbitmq.host}")
    private String host;
    @Value("${spring.rabbitmq.port}")
    private int    port;
    @Value("${spring.rabbitmq.username}")
    private String userName;
    @Value("${spring.rabbitmq.password}")
    private String password;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // the HTTP URL for the endpoint to which a WebSocket client will need to connect to
        // for the WebSocket handshake
        registry.addEndpoint("/ws");
    }

    // TODO: ApplicationContext event listeners
    // https://docs.spring.io/spring-framework/docs/current/reference/html/web.html#websocket-stomp-appplication-context-events

    // TODO: Intercept messages if needed
    // https://docs.spring.io/spring-framework/docs/current/reference/html/web.html#websocket-stomp-interceptors
    // incoming from client
    static class InboundDebugInterceptor implements ChannelInterceptor {

        @Override
        public Message<?> preSend(Message<?> message, MessageChannel channel) {
            StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
            return message;
        }
    }

    // outgoing to client
    static class OutboundDebugInterceptor implements ChannelInterceptor {

        @Override
        public Message<?> preSend(Message<?> message, MessageChannel channel) {
            StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
            return message;
        }
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new InboundDebugInterceptor());
    }

    @Override
    public void configureClientOutboundChannel(ChannelRegistration registration) {
        registration.interceptors(new OutboundDebugInterceptor());
    }
    // END intercept

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // STOMP messages whose destination header begins with these prefixes
        // are routed to @MessageMapping methods in @Controller classes,
        // before they're sent to the message broker?
        registry.setApplicationDestinationPrefixes("/app");

        // TODO: https://docs.spring.io/spring-framework/docs/current/reference/html/web.html#websocket-stomp-handle-broker-relay-configure
        // STOMP messages whose destination header begins with these prefixes
        // are routed directly to the message broker.
        registry.enableStompBrokerRelay("/topic", "/queue")
                .setRelayHost(host)
                .setRelayPort(port)
                .setSystemLogin(userName)
                .setSystemPasscode(password);

        // use a simple broker from spring
        //         registry.enableSimpleBroker("/topic", "/queue");


        // Messages from the broker are published to the clientOutboundChannel,
        // from where they are written to WebSocket sessions.
        // As the channel is backed by a ThreadPoolExecutor, messages are processed
        // in different threads, and the resulting sequence received by the client
        // may not match the exact order of publication.
        //
        //If this is an issue, enable the setPreservePublishOrder flag
        registry.setPreservePublishOrder(true);
    }

    // TODO: configure time limit and buffer size, if needed
    //	@Override
    //	public void configureWebSocketTransport(WebSocketTransportRegistration registry) {
    //		registry.setSendTimeLimit(15 * 1000)
    //				.setSendBufferSizeLimit(512 * 1024);
    //	}
}
