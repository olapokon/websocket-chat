package ak4ra.websocketchat.events;

/**
 * Holds the simp session id and destination of a simp session.
 */
public record SessionDestination(String simpSessionId,
                                 String destination) {}
