package ak4ra.websocketchat.chatroom;

import java.util.Set;

/**
 * Represents a chatroom.
 *
 * @param name
 *         the name of the chatroom
 * @param endpoint
 *         to be appended to the message broker destination
 * @param users
 *         the github ids of the users who are authorized to access the chatroom
 */
public record Chatroom(Long id,
                       String name,
                       String endpoint,
                       Set<Long> users) {}
