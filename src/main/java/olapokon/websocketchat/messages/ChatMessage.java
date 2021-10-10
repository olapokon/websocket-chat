package olapokon.websocketchat.messages;

/**
 * The body of a STOMP message to be sent to a chatroom.
 *
 * @param type
 *         the type of the message
 * @param sender
 *         the username of the user sending the message, or on whose behalf the message is sent (e.g. when the user
 *         joins a channel)
 * @param message
 *         a JSON string containing the text input by the chat user, a {@link ChatroomEvent} value, if the type is
 *         {@link ChatMessageType#CHATROOM_MESSAGE}, or a list of the usernames of the connected {@link
 *         olapokon.websocketchat.entities.User}s, if the type is {@link ChatMessageType#USER_LIST_UPDATE}
 * @param timeStamp
 *         the time the message is sent
 */
public record ChatMessage(ChatMessageType type,
                          String sender,
                          String message,
                          String timeStamp) {}
