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

public class InboundChannelInterceptor implements ChannelInterceptor {

    private static final Logger         log = LoggerFactory.getLogger(InboundChannelInterceptor.class);
    private final        MessageService messageService;

    public InboundChannelInterceptor(MessageService messageService) {
        this.messageService = messageService;
    }

    // When you throw any exception from ClientInboundChannelInterceptor, it will be sent as ERROR frame
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        SimpMessageType type = accessor.getMessageType();

        if (type != SimpMessageType.HEARTBEAT) {
            log.info("inbound message type: {}", accessor.getMessageType());
        }

        return message;
    }
}
