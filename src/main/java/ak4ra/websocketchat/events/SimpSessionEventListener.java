package ak4ra.websocketchat.events;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import ak4ra.websocketchat.entities.User;
import ak4ra.websocketchat.exceptions.InvalidStateException;
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
    public void sessionSubscribe(SessionSubscribeEvent e) {
        log.info("sessions currently in the map: {}", simpSessionDestinations);
        Message<?> m = e.getMessage();
        User user = SimpMessageHeadersUtil.extractUser(m);
        SessionDestination sessDest = SimpMessageHeadersUtil.extractSessionDestination(m);
        Set<SessionDestination> val = new HashSet<>(List.of(sessDest));

        simpSessionDestinations.merge(user, val, (sessDests1, sessDests2) -> {
            sessDests1.addAll(sessDests2);
            return sessDests1;
        });
        log.warn("user {} connected to {}", user.getUsername(), sessDest.destination());
    }

    @EventListener
    public void sessionDisconnect(SessionDisconnectEvent e) {
        Message<?> m = e.getMessage();
        User user = SimpMessageHeadersUtil.extractUser(m);
        SessionDestination sessDest = SimpMessageHeadersUtil.extractSessionDestination(m);

        // the session that was just disconnected will still be in the map, holding its destination
        Set<SessionDestination> sessDests = simpSessionDestinations.get(user);
        SessionDestination disconnectedSession = sessDests
                .stream()
                .filter(s -> s.simpSessionId().equals(sessDest.simpSessionId()))
                .findFirst()
                .orElseThrow(() -> new InvalidStateException("Simp session missing."));

        // remove it from the map
        simpSessionDestinations.get(user).remove(disconnectedSession);

        // TODO: send notification to the destination that the user has disconnected
        String disconnectedDestination = disconnectedSession.destination();
        log.warn("user {} disconnected from {}", user.getUsername(), disconnectedDestination);
    }
}
