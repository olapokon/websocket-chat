package ak4ra.websocketchat.presence;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import ak4ra.websocketchat.entities.User;
import ak4ra.websocketchat.exceptions.InvalidStateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class UserPresenceTracker {

    private final Logger log = LoggerFactory.getLogger(UserPresenceTracker.class);

    /**
     * Holds the {@link SessionDestination} pairs for currently connected {@link User}s.
     */
    private final ConcurrentHashMap<User, Set<SessionDestination>> simpSessionDestinations =
            new ConcurrentHashMap<>();

    /**
     * Adds a {@link SessionDestination}.
     *
     * @param u
     *         the user
     * @param sd
     *         the session/destination
     */
    public void addSessionDestination(User u, SessionDestination sd) {
        Set<SessionDestination> val = new HashSet<>(List.of(sd));
        simpSessionDestinations.merge(u, val, (sds1, sds2) -> {
            sds2.addAll(sds1);
            return sds2;
        });
        log.info("user {} connected to {}", u.getUsername(), sd.destination());
        logTrackerState();
    }

    /**
     * Removes a {@link SessionDestination} and returns its destination.
     * <p>
     * This is necessary in order to know which destination/chatroom the simpSession that is currently disconnecting was
     * connected. The reason is that the destination is not available in a disconnect message.
     *
     * @param u
     *         the user
     * @param sd
     *         the session/destination
     *
     * @throws InvalidStateException
     *         if the {@link SessionDestination} is not present
     */
    public String removeSessionDestination(User u, SessionDestination sd) {
        AtomicReference<String> destination = new AtomicReference<>();
        simpSessionDestinations.compute(u, (user, sessionDestinations) -> {
            if (sessionDestinations == null)
                throw new InvalidStateException("Simp session missing.");
            SessionDestination disconnected = sessionDestinations
                    .stream()
                    .filter(s -> s.simpSessionId().equals(sd.simpSessionId()))
                    .findFirst()
                    .orElseThrow(() -> new InvalidStateException("Simp session missing."));
            sessionDestinations.remove(disconnected);
            destination.set(disconnected.destination());
            return sessionDestinations;
        });

        log.info("user {} disconnected from {}", u.getUsername(), destination);
        logTrackerState();
        return destination.get();
    }

    private void logTrackerState() {
        String formatted = formatMap(simpSessionDestinations);
        log.info("user presence: {}", formatted);
    }

    private static String formatMap(ConcurrentHashMap<User, Set<SessionDestination>> m) {
        return m.entrySet()
                .stream()
                .map(entry -> "\n" + entry.getKey().toString()
                              + entry.getValue()
                                     .stream()
                                     .map(sd -> "\n\t" + sd.toString())
                                     .collect(Collectors.joining()))
                .collect(Collectors.joining());
    }
}
