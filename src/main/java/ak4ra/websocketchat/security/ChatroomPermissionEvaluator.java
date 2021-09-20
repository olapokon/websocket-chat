package ak4ra.websocketchat.security;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;

public class ChatroomPermissionEvaluator implements PermissionEvaluator {

    private final Logger log = LoggerFactory.getLogger(ChatroomPermissionEvaluator.class);

    /**
     * Determines if the user has permission to access a given chatroom.
     *
     * @param authentication
     *         the current {@link Authentication}
     * @param targetObject
     *         the id of the chatroom the user is trying to access
     * @param permission
     *         it is always null
     *
     * @return true if the user has permission to access the chatroom
     */
    @Override
    public boolean hasPermission(Authentication authentication,
                                 Object targetObject,
                                 Object permission) {
        log.info("hasPermission:\n"
                           + "\tAuthentication: " + authentication.toString() + "\n"
                           + "\tChatroom id: " + targetObject);
        String userId = authentication.getName();
        log.info("authentication: {}", authentication);
        log.info("userId: {}", userId);
        // TODO: chatroom exists and has the user id in its "users" set
        return true;
    }

    /**
     * Unimplemented, the other overload is used instead.
     */
    @Override
    @Deprecated(since = "0.0")
    public boolean hasPermission(Authentication authentication,
                                 Serializable targetID,
                                 String type,
                                 Object permission) {
        return false;
    }
}
