package olapokon.websocketchat.messages;

import olapokon.websocketchat.util.SimpMessageHeadersUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;

// TODO: probably needs to be removed
public class InboundChannelInterceptor implements ChannelInterceptor {

    private static final Logger         log = LoggerFactory.getLogger(InboundChannelInterceptor.class);
    private final        MessageService messageService;

    public InboundChannelInterceptor(MessageService messageService) {
        this.messageService = messageService;
    }

    // When you throw any exception from ClientInboundChannelInterceptor, it will be sent as an ERROR frame
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        SimpMessageType type = accessor.getMessageType();
        if (type == SimpMessageType.HEARTBEAT)
            return message;

        log.debug("inbound STOMP message type: {}", accessor.getMessageType());
        String t = SimpMessageHeadersUtil.getChatMessageType(message);
        if (t != null)
            log.debug("inbound STOMP message ChatMessageType: {}", t);
        return message;
    }
}
