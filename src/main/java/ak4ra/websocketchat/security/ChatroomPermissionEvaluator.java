package ak4ra.websocketchat.security;

import java.io.Serializable;

import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;

public class ChatroomPermissionEvaluator implements PermissionEvaluator {

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
        System.out.println("hasPermission:\n"
                           + "\tAuthentication: " + authentication.toString() + "\n"
                           + "\tChatroom id: " + targetObject);
        return false;
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
