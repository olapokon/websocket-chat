package ak4ra.websocketchat.events;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import ak4ra.websocketchat.entities.User;
import ak4ra.websocketchat.util.SimpMessageHeadersUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

/**
 * Contains listeners for simp session events, in order to track the user's entering and leaving chatrooms.
 */
@Component
public class SimpSessionEventListener {

    private final Logger log = LoggerFactory.getLogger(SimpSessionEventListener.class);

    private final ConcurrentHashMap<User, Set<SessionDestination>> simpSessionDestinations =
            new ConcurrentHashMap<>();

    @EventListener
    public void sessionConnect(SessionSubscribeEvent e) {
        Message<?> m = e.getMessage();
        User user = SimpMessageHeadersUtil.extractUser(m);
        SessionDestination sessDest = SimpMessageHeadersUtil.extractSessionDestination(m);
        Set<SessionDestination> val = new HashSet<>(List.of(sessDest));

        simpSessionDestinations.merge(user, val, (sessDests1, sessDests2) -> {
            sessDests1.addAll(sessDests2);
            return sessDests1;
        });
    }

    @EventListener
    public void sessionDisconnect(SessionDisconnectEvent e) {
        Message<?> m = e.getMessage();
        User user = SimpMessageHeadersUtil.extractUser(m);
        SessionDestination sessDest = SimpMessageHeadersUtil.extractSessionDestination(m);

        Set<SessionDestination> sessDests = simpSessionDestinations.get(user);

    }
}
