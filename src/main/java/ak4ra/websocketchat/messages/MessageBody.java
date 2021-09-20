package ak4ra.websocketchat.messages;

/**
 * The body of a STOMP message to be sent to a chatroom.
 *
 * @param type
 *         the type of the message
 * @param sender
 *         the username of the user, or the id of the channel, if the type is {@link ChatMessage#CHANNEL_EVENT}
 * @param message
 *         the text input by the chat user, or a {@link ChannelEvent} value, if the type is {@link
 *         ChatMessage#CHANNEL_EVENT}
 * @param timeStamp
 *         the time the message is sent
 */
public record MessageBody(ChatMessage type,
                          String sender,
                          String message,
                          String timeStamp) {}
