package olapokon.websocketchat.messages;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.lang.Nullable;

/**
 * Enumeration of custom STOMP headers.
 */
public class CustomStompHeaders {

    private final Map<String, Object> headers = new LinkedHashMap<>();

    /**
     * A header that specifies the type of the STOMP message.
     * <p>
     * The value of this header is one of {@link ChatMessageType}.
     */
    public static final String MESSAGE_TYPE = "message-type";

    /**
     * A header carrying the timestamp of a chatroom message.
     * <p>
     * The value of this header is a {@link java.time.ZonedDateTime} string.
     */
    public static final String TIMESTAMP = "chat-timestamp";

    /**
     * Adds a {@link CustomStompHeaders#MESSAGE_TYPE} header.
     *
     * @param type the {@link ChatMessageType} of the STOMP message.
     * @param value a value required by some of the message types
     */
    public CustomStompHeaders addMessageType(ChatMessageType type, @Nullable String value) {
        String v = switch (type) {
            case USER_MESSAGE -> ChatMessageType.USER_MESSAGE.value + "; " + value;
            case USER_JOINED -> ChatMessageType.USER_JOINED.value + "; " + value;
            case USER_LEFT -> ChatMessageType.USER_LEFT.value + "; " + value;
            case USER_LIST_UPDATE -> ChatMessageType.USER_LIST_UPDATE.value + "; " + value;
            case USER_LIST_UPDATE_REQUEST -> ChatMessageType.USER_LIST_UPDATE_REQUEST.value;
        };
        this.headers.put(MESSAGE_TYPE, v);
        return this;
    }

    /**
     * Adds a {@link CustomStompHeaders#TIMESTAMP} header.
     *
     * @param zonedDateTimeString a date {@link String}
     */
    public CustomStompHeaders addTimestamp(String zonedDateTimeString) {
        this.headers.put(TIMESTAMP, zonedDateTimeString);
        return this;
    }

    public Map<String, Object> build() {
        return headers;
    }
}
