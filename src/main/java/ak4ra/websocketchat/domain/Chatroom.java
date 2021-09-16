package ak4ra.websocketchat.domain;

/**
 * Represents a chatroom.
 *
 * @param name
 *         The name of the chatroom.
 * @param endpoint
 *         To be appended to the message broker destination.
 */
public record Chatroom(String name,
                       String endpoint) {}
