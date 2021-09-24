package ak4ra.websocketchat.messages;

import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;

public class InboundChannelInterceptor implements ChannelInterceptor {

    private static final Logger         log = LoggerFactory.getLogger(InboundChannelInterceptor.class);
    private final        MessageService messageService;

    public InboundChannelInterceptor(MessageService messageService) {
        this.messageService = messageService;
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        SimpMessageType type = accessor.getMessageType();
        String destination = accessor.getDestination();
        OAuth2AuthenticationToken oauth2Token = (OAuth2AuthenticationToken) accessor.getUser();
        Map<String, Object> userAttributes = oauth2Token.getPrincipal().getAttributes();
        String simpSessionId = accessor.getSessionId();

        //        switch (type) {
        //            case HEARTBEAT -> {}
        //            case SUBSCRIBE -> {
        //                // when a user connects, send notification to the chatroom
        //                try {
        //                    messageService.sendUserJoinedMessage(destination, userAttributes);
        //                } catch (JsonProcessingException e) {
        //                    log.error("Failed to send user SUBSCRIBE notification message");
        //                    e.printStackTrace();
        //                }
        //            }
        //            case DISCONNECT -> {
        //                log.info("DISCONNECT accessor: {}", accessor);
        //                log.info("DISCONNECT simpSessionId: {}", simpSessionId);
        //                log.info("DISCONNECT destination: {}", destination);
        //                log.info("DISCONNECT user attributes: {}", userAttributes);
        //                // when a user disconnects, send notification to the chatroom
        //                //            try {
        //                //                messageService.sendUserLeftMessage(destination, userAttributes);
        //                //            } catch (JsonProcessingException e) {
        //                //                log.error("Failed to send user DISCONNECT notification message");
        //                //                e.printStackTrace();
        //                //            }
        //            }
        //            default -> log.info("inbound message type: {}", type);
        //        }
        if (type != SimpMessageType.HEARTBEAT) {
            log.info("inbound message type: {}", accessor.getMessageType());
        }
        if (type == SimpMessageType.SUBSCRIBE) {
            // when a user connects, send notification to the chatroom
            try {
                messageService.sendUserJoinedMessage(destination, userAttributes);
            } catch (JsonProcessingException e) {
                log.error("Failed to send user SUBSCRIBE notification message");
                e.printStackTrace();
            }
        }
        if (type == SimpMessageType.DISCONNECT) {
            log.info("DISCONNECT accessor: {}", accessor);
            log.info("DISCONNECT simpSessionId: {}", simpSessionId);
            log.info("DISCONNECT destination: {}", destination);
            log.info("DISCONNECT user attributes: {}", userAttributes);
            // when a user disconnects, send notification to the chatroom
            // TODO: fox disconnect notification
            if (destination == null) {
                log.error("Could not send DISCONNECT notification, destination was null.");
            } else {
                try {
                    messageService.sendUserLeftMessage(userAttributes);
                } catch (JsonProcessingException e) {
                    log.error("Failed to send user DISCONNECT notification message");
                    e.printStackTrace();
                }
            }
        }

        return message;
    }
}
