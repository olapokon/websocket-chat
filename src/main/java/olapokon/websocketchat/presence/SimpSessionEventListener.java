package olapokon.websocketchat.presence;

import olapokon.websocketchat.entities.User;
import olapokon.websocketchat.user.UserService;
import olapokon.websocketchat.util.SimpMessageHeadersUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

/**
 * Listens for simp session events.
 */
@Component
public class SimpSessionEventListener {

    private final UserPresenceTracker userPresenceTracker;
    private final UserService         userService;

    @Autowired
    public SimpSessionEventListener(UserPresenceTracker userPresenceTracker,
                                    UserService userService) {
        this.userPresenceTracker = userPresenceTracker;
        this.userService = userService;
    }

    @EventListener
    public void sessionSubscribe(SessionSubscribeEvent e) throws JsonProcessingException {
        Message<?> m = e.getMessage();
        User user = SimpMessageHeadersUtil.extractUser(m);
        SessionDestination sd = SimpMessageHeadersUtil.extractSessionDestination(m);

        final boolean sendJoinNotification = userPresenceTracker.addSessionDestination(user, sd);
        if (sendJoinNotification)
            userService.userJoinChatroom(user, sd.destination());
    }

    @EventListener
    public void sessionDisconnect(SessionDisconnectEvent e) throws JsonProcessingException {
        Message<?> m = e.getMessage();
        User user = SimpMessageHeadersUtil.extractUser(m);
        // On a disconnect, there is no destination information in the simp message headers.
        // The SessionDestination returned from the util has an empty destination.
        SessionDestination sd = SimpMessageHeadersUtil.extractSessionDestination(m);

        // The actual destination needs to be retrieved from the tracker, where the destination for
        // the session currently disconnecting is stored.
        String destination = userPresenceTracker.removeSessionDestination(user, sd);
        if (destination != null)
            userService.userLeaveChatroom(user, destination);
    }
}
