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

/**
 * {@inheritDoc}
 * <p>
 * An in-memory implementation of {@link UserPresenceTracker}.
 */
@Component
public class InMemoryUserPresenceTracker implements UserPresenceTracker {

    private final Logger log = LoggerFactory.getLogger(InMemoryUserPresenceTracker.class);

    /**
     * Holds the {@link SessionDestination} pairs for currently connected {@link User}s.
     * <p>
     * The same user can be connected to the same chatroom more than once, through a different simp session.
     */
    private final ConcurrentHashMap<User, Set<SessionDestination>> simpSessionDestinations =
            new ConcurrentHashMap<>();


    /**
     * {@inheritDoc}
     */
    // TODO: user opens same chatroom in another tab - do not send notification
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
     * {@inheritDoc}
     */
    // TODO: user leaves chatroom that is open in another tab - do not send notification
    public String removeSessionDestination(User u, SessionDestination sd) throws InvalidStateException {
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
            if (sessionDestinations.isEmpty())
                return null;
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
