package ak4ra.websocketchat.presence;

import java.util.Set;

import ak4ra.websocketchat.entities.Chatroom;
import ak4ra.websocketchat.entities.User;
import ak4ra.websocketchat.exceptions.InvalidStateException;
import org.springframework.lang.Nullable;
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
     *
     * @return true if the user just joined a chatroom by connecting to this destination, false if the user is already
     *         connected to the chatroom through a different simp session (e.g. from a different browser window)
     */
    boolean addSessionDestination(User u, SessionDestination sd);

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
     * @return the destination of the simp session that is disconnecting or returns null if the user has other active
     *         simp sessions to that destination
     *
     * @throws InvalidStateException
     *         if the {@link SessionDestination} is not present
     */
    @Nullable
    String removeSessionDestination(User u, SessionDestination sd) throws InvalidStateException;

    /**
     * Returns all {@link User}s currently connected to the given chatroom.
     *
     * @param chatroom
     *         the chatroom for which the user list is being requested
     *
     * @return the chatroom's list of users
     */
    Set<User> getUserList(Chatroom chatroom);
}
