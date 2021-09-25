package ak4ra.websocketchat.util;

import java.util.Map;

import ak4ra.websocketchat.entities.User;
import ak4ra.websocketchat.entities.UserType;
import ak4ra.websocketchat.presence.SessionDestination;
import ak4ra.websocketchat.exceptions.ValidationException;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;

/**
 * Utilities for extracting information from {@link Message} headers.
 */
public final class SimpMessageHeadersUtil {

    private SimpMessageHeadersUtil() {}

    /**
     * Extracts a {@link User} from the headers of a {@link Message}.
     *
     * @param m
     *         the message
     *
     * @return the extracted user
     *
     * @throws ValidationException
     *         if any of the expected fields is not found in the headers of the message
     */
    public static User extractUser(Message<?> m) {
        SimpMessageHeaderAccessor accessor = SimpMessageHeaderAccessor.wrap(m);

        try {
            OAuth2AuthenticationToken oauth2Token = ((OAuth2AuthenticationToken) accessor.getUser());
            Map<String, Object> attributes = oauth2Token.getPrincipal().getAttributes();
            String authorizedClientRegistrationId = oauth2Token.getAuthorizedClientRegistrationId();
            String providedId = attributes.get("id").toString(); // github id or equivalent
            String username = attributes.get("login").toString(); // github username or equivalent
            UserType type = determineUserType(authorizedClientRegistrationId);
            return new User(type, providedId, username);
        } catch (Exception ignored) {
            throw new ValidationException("Invalid simp message headers");
        }
    }

    /**
     * Extracts a {@link SessionDestination} from the headers of a {@link Message}.
     *
     * @param m
     *         the message
     *
     * @return the extracted SessionDestination
     *
     * @throws ValidationException
     *         if any of the expected fields is not found in the headers of the message
     */
    public static SessionDestination extractSessionDestination(Message<?> m) {
        SimpMessageHeaderAccessor accessor = SimpMessageHeaderAccessor.wrap(m);

        try {
            String simpSessionId = accessor.getSessionId();
            String destination = accessor.getDestination();
            return new SessionDestination(simpSessionId, destination);
        } catch (Exception ignored) {
            throw new ValidationException("Invalid simp message headers");
        }
    }

    /**
     * Maps an authorizedClientRegistrationId extracted from a {@link Message} header to a {@link UserType} or throws an
     * exception.
     *
     * @param authorizedClientRegistrationId
     *         a {@link String} extracted from the headers
     *
     * @return the matching {@link UserType}
     *
     * @throws ValidationException
     *         if any of the expected fields is not found in the headers of the message
     */
    private static UserType determineUserType(String authorizedClientRegistrationId) {
        return switch (authorizedClientRegistrationId.toUpperCase()) {
            case "GITHUB" -> UserType.GITHUB;
            default -> throw new ValidationException("Invalid authorizedClientRegistrationId");
        };
    }
}
