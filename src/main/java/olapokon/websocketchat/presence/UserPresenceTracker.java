package olapokon.websocketchat.presence;

import java.util.List;

import olapokon.websocketchat.entities.User;
import olapokon.websocketchat.exceptions.InvalidStateException;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

/**
 * Tracks the message broker destinations each user is connected to.
 */
@Component
public interface UserPresenceTracker {

    /**
     * Adds a {@link SessionDestination} to the tracker.
     *
     * @param user
     *         the user
     * @param sessionDestination
     *         the session/destination
     *
     * @return true if the user just joined a chatroom by connecting to this destination, false if the user is already
     *         connected to the chatroom through a different simp session (e.g. from a different browser window)
     */
    boolean addSessionDestination(User user, SessionDestination sessionDestination);

    /**
     * Removes a {@link SessionDestination} from the tracker and returns its destination.
     * <p>
     * Returning the destination is necessary in order to know which destination/chatroom the simpSession that is
     * currently disconnecting was connected to. The reason is that the destination is not available in a disconnect
     * message.
     *
     * @param user
     *         the user
     * @param sessionDestination
     *         the session/destination
     *
     * @return the destination of the simp session that is disconnecting or returns null if the user has other active
     *         simp sessions to that destination
     *
     * @throws InvalidStateException
     *         if the {@link SessionDestination} is not present
     */
    @Nullable
    String removeSessionDestination(User user, SessionDestination sessionDestination) throws InvalidStateException;

    /**
     * Returns all {@link User}s currently connected to the given chatroom.
     *
     * @param destination
     *         the chatroom 's destination in the message broker, for which the user list is being requested
     *
     * @return the chatroom's list of users
     */
    List<User> getUserList(String destination);
}
