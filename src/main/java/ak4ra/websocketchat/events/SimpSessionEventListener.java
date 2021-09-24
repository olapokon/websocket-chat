package ak4ra.websocketchat.events;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

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
        Message<?> m = e.getMessage();
        User user = SimpMessageHeadersUtil.extractUser(m);
        SessionDestination sessDest = SimpMessageHeadersUtil.extractSessionDestination(m);
        Set<SessionDestination> val = new HashSet<>(List.of(sessDest));

        simpSessionDestinations.merge(user, val, (sessDests1, sessDests2) -> {
            sessDests1.addAll(sessDests2);
            return sessDests1;
        });
        log.warn("user {} connected to {}", user.getUsername(), sessDest.destination());
        String map = formatMap(simpSessionDestinations);
        log.info("sessions currently in the map: {}", map);
    }

    @EventListener
    public void sessionDisconnect(SessionDisconnectEvent e) {
        Message<?> m = e.getMessage();
        User user = SimpMessageHeadersUtil.extractUser(m);
        SessionDestination sessDest = SimpMessageHeadersUtil.extractSessionDestination(m);

        // the session that was just disconnected must be in the map, holding its destination
        AtomicReference<String> destination = new AtomicReference<>();
        simpSessionDestinations.compute(user, (user1, sessionDestinations) -> {
            if (sessionDestinations == null) {
                throw new InvalidStateException("Simp session missing.");
            }
            SessionDestination disconnected = sessionDestinations
                    .stream()
                    .filter(s -> s.simpSessionId().equals(sessDest.simpSessionId()))
                    .findFirst()
                    .orElseThrow(() -> new InvalidStateException("Simp session missing."));
            sessionDestinations.remove(disconnected);
            destination.set(disconnected.destination());
            return sessionDestinations;
        });

        // TODO: send notification to the destination that the user has disconnected
        log.warn("user {} disconnected from {}", user.getUsername(), destination);
        String map = formatMap(simpSessionDestinations);
        log.info("sessions currently in the map: {}", map);
    }

    private static String formatMap(ConcurrentHashMap<User, Set<SessionDestination>> m) {
        return m
                .entrySet()
                .stream()
                .map(entry -> "\n" + entry.getKey().toString()
                              + entry.getValue()
                                     .stream()
                                     .map(sd -> "\n\t" + sd.toString())
                                     .collect(Collectors.joining()))
                .collect(Collectors.joining());
    }
}
