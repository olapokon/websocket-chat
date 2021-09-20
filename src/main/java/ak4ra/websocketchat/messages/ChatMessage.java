package ak4ra.websocketchat.messages;

/**
 * Possible types of a STOMP message.
 */
public enum ChatMessage {
    /**
     * The message is sent by a chat room user.
     */
    USER_MESSAGE,

    /**
     * The message is sent by the chat server due to a channel event.
     */
    CHANNEL_EVENT,
}
