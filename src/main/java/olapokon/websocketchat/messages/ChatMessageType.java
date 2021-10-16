package olapokon.websocketchat.messages;

/**
 * Possible types of a {@link ChatMessage}.
 */
public enum ChatMessageType {
    /**
     * A message sent by a chat room user. The text of the message should be in the STOMP frame body.
     */
    USER_MESSAGE,

    /**
     * The message is sent by the chat server due to a channel event.
     */
    CHATROOM_MESSAGE,

    /**
     * The message is sent by the chat server, containing the updated list of users as its body.
     */
    USER_LIST_UPDATE,
}
