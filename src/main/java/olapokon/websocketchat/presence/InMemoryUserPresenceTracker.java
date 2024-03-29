package olapokon.websocketchat.presence;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import olapokon.websocketchat.entities.User;
import olapokon.websocketchat.exceptions.InvalidStateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

/**
 * {@inheritDoc}
 * <p>
 * An in-memory implementation of {@link UserPresenceTracker}.
 * <p>
 * A different tracker should be implemented eventually.
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
    public boolean addSessionDestination(User u, SessionDestination sd) {
        AtomicBoolean userJoinedChatroom = new AtomicBoolean(true);
        Set<SessionDestination> val = new CopyOnWriteArraySet<>(List.of(sd));
        simpSessionDestinations.compute(u, (user, sessionDestinations) -> {
            if (sessionDestinations == null)
                return val;
            Set<SessionDestination> sameDestinationSds = sessionDestinations
                    .stream()
                    .filter(sessD -> sessD.destination().equals(sd.destination()))
                    .collect(Collectors.toSet());
            if (!sameDestinationSds.isEmpty()) {
                // the user is already connected to this destination with a different simp session
                userJoinedChatroom.set(false);
            }
            sessionDestinations.addAll(val);
            return sessionDestinations;
        });
        final String username = u.getUsername();
        final String destination = sd.destination();
        if (userJoinedChatroom.get())
            log.debug("user {} connected to {}", username, destination);
        else
            log.debug("user {} opened an additional connection to {}", username, destination);
        logTrackerState();
        return userJoinedChatroom.get();
    }

    /**
     * {@inheritDoc}
     */
    public @Nullable
    String removeSessionDestination(User u, SessionDestination sd) throws InvalidStateException {
        AtomicBoolean userLeftChatroom = new AtomicBoolean(true);
        AtomicReference<String> destination = new AtomicReference<>();
        simpSessionDestinations.compute(u, (user, sessionDestinations) -> {
            if (sessionDestinations == null)
                throw new InvalidStateException("Simp session missing.");
            SessionDestination disconnected = sessionDestinations
                    .stream()
                    .filter(s -> s.simpSessionId().equals(sd.simpSessionId()))
                    .findFirst()
                    .orElseThrow(() -> new InvalidStateException("Simp session missing."));
            Set<SessionDestination> sameDestinationSds = sessionDestinations
                    .stream()
                    .filter(sessD -> sessD.destination().equals(disconnected.destination()))
                    .collect(Collectors.toSet());
            if (sameDestinationSds.size() > 1)
                // the user is connected to this destination with more than one simp sessions
                userLeftChatroom.set(false);
            sessionDestinations.remove(disconnected);
            destination.set(disconnected.destination());
            if (sessionDestinations.isEmpty())
                return null;
            return sessionDestinations;
        });

        logTrackerState();
        final String username = u.getUsername();
        if (userLeftChatroom.get()) {
            log.debug("user {} disconnected from {}", username, destination);
            return destination.get();
        } else {
            log.debug("user {} closed one of their connections", username);
            return null;
        }
    }

    /**
     * {@inheritDoc}
     * <p>
     *
     * @deprecated Inefficient as it goes through all active simp sessions. The data structures should be refactored or
     * a different {@link UserPresenceTracker} implementation should be used instead.
     */
    @Deprecated(since = "0.0")
    public List<User> getUserList(String destination) {
        return simpSessionDestinations
                .entrySet()
                .stream()
                .filter(entry -> entry.getValue()
                        .stream()
                        .anyMatch(sd -> sd.destination() != null
                                && sd.destination().equals(destination)))
                .map(Map.Entry::getKey)
                .distinct()
                .sorted((u1, u2) -> u1.getUsername().compareToIgnoreCase(u2.getUsername()))
                .toList();
    }

    /**
     * @deprecated Only use in development.
     */
    @Deprecated(since = "0.0")
    private void logTrackerState() {
        String formatted = formatMap(simpSessionDestinations);
        log.trace("user presence: {}", formatted);
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
