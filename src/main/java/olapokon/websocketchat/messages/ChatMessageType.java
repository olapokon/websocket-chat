package olapokon.websocketchat.messages;

/**
 * Possible types of a custom STOMP message sent by the chat server or a chatroom client.
 * <p>
 * It is used as the value for the {@link CustomStompHeaders#MESSAGE_TYPE} header.
 */
public enum ChatMessageType {
    /**
     * A message sent by a chat room user.
     * <p>
     * The username of the user should follow after a "; ".
     * <p>
     * The text of the message should be in the STOMP frame's body.
     */
    USER_MESSAGE("user-message"),

    /**
     * A message that indicates a user has joined the chatroom.
     * <p>
     * The username of the user should follow after a "; ".
     */
    USER_JOINED("user-joined"),

    /**
     * A message that indicates a user has left the chatroom.
     * <p>
     * The username of the user should follow after a "; ".
     */
    USER_LEFT("user-left"),

    /**
     * A message sent by a chat server, containing an updated list of users.
     * <p>
     * The JSON string converted {@link java.util.List} of usernames should follow after a "; ".
     */
    USER_LIST_UPDATE("user-list-update"),

    /**
     * A message sent by a chat client, requesting a {@link ChatMessageType#USER_LIST_UPDATE} message.
     */
    USER_LIST_UPDATE_REQUEST("user-list-update-request");

    public final String value;

    ChatMessageType(String value) {
        this.value = value;
    }
}
