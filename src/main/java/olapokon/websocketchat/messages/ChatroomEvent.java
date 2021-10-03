package olapokon.websocketchat.messages;

/**
 * The type of a chatroom event. It is used to populate the message field of a {@link ChatMessage}, if it is of type
 * {@link ChatMessageType#CHATROOM_MESSAGE}
 */
public enum ChatroomEvent {
    USER_JOINED,
    USER_LEFT,
}
