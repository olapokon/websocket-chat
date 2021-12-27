package olapokon.websocketchat.presence;

import org.springframework.lang.Nullable;

/**
 * Holds the simp session id and destination of a simp session.
 */
public record SessionDestination(@Nullable String simpSessionId,
                                 @Nullable String destination) {}
