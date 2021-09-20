package ak4ra.websocketchat.messages;

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

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        SimpMessageType type = accessor.getMessageType();
        if (type != SimpMessageType.HEARTBEAT) {
            log.info("inbound message type: {}", accessor.getMessageType());
        }
        if (type == SimpMessageType.CONNECT) {
            // when a user connects, send notification to the chatroom
            // TODO
            log.info("{}", accessor.getMessageHeaders());
            //            messageService.sendChatroomMessage(ChatroomEvent.USER_LEFT, chatroomId, username);
        }
        if (type == SimpMessageType.DISCONNECT) {
            // when a user disconnects, send notification to the chatroom
            // TODO
            log.info("{}", accessor.getMessageHeaders());
        }
        return message;
    }
}
