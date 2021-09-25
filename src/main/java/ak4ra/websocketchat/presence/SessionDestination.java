package ak4ra.websocketchat.userpresence;

/**
 * Holds the simp session id and destination of a simp session.
 */
public record SessionDestination(String simpSessionId,
                                 String destination) {}