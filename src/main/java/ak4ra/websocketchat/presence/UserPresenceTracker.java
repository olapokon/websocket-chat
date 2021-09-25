package ak4ra.websocketchat.presence;

import ak4ra.websocketchat.entities.User;
import ak4ra.websocketchat.exceptions.InvalidStateException;
import org.springframework.stereotype.Component;

/**
 * Tracks the chatrooms each user is connected to.
 */
@Component
public interface UserPresenceTracker {

    /**
     * Adds a {@link SessionDestination} to the tracker.
     *
     * @param u
     *         the user
     * @param sd
     *         the session/destination
     */
    void addSessionDestination(User u, SessionDestination sd);

    /**
     * Removes a {@link SessionDestination} from the tracker and returns its destination.
     * <p>
     * Returning the destination is necessary in order to know which destination/chatroom the simpSession that is
     * currently disconnecting was connected to. The reason is that the destination is not available in a disconnect
     * message.
     *
     * @param u
     *         the user
     * @param sd
     *         the session/destination
     *
     * @throws InvalidStateException
     *         if the {@link SessionDestination} is not present
     */
    String removeSessionDestination(User u, SessionDestination sd) throws InvalidStateException;
}
