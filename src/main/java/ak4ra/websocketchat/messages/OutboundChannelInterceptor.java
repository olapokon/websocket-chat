package ak4ra.websocketchat.messages;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;

public class OutboundChannelInterceptor implements ChannelInterceptor {

    private static final Logger         log = LoggerFactory.getLogger(OutboundChannelInterceptor.class);
    private final        MessageService messageService;

    public OutboundChannelInterceptor(MessageService messageService) {
        this.messageService = messageService;
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        SimpMessageType type = accessor.getMessageType();
        if (type != SimpMessageType.HEARTBEAT) {
            log.info("outbound message type: {}", accessor.getMessageType());
        }
        return message;
    }
}
