package olapokon.websocketchat.messages;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

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
        // the HTTP URL for the WebSocket handshake
        registry.addEndpoint("/ws");
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new InboundChannelInterceptor());
    }

    @Override
    public void configureClientOutboundChannel(ChannelRegistration registration) {
        registration.interceptors(new OutboundChannelInterceptor());
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // STOMP frames whose destination header begins with these prefixes
        // are routed to @MessageMapping methods in @Controller classes
        registry.setApplicationDestinationPrefixes("/app");

        // STOMP frames whose destination header begins with these prefixes
        // are routed directly to the message broker.
        registry.enableStompBrokerRelay("/topic")
                .setRelayHost(host)
                .setRelayPort(port)
                .setSystemLogin(userName)
                .setSystemPasscode(password);


        // Messages from the broker are published to the clientOutboundChannel,
        // from where they are written to WebSocket sessions.
        // As the channel is backed by a ThreadPoolExecutor, messages are processed
        // in different threads, and the resulting sequence received by the client
        // may not match the exact order of publication.
        //
        //If this is an issue, enable the setPreservePublishOrder flag
        registry.setPreservePublishOrder(true);
    }
}
