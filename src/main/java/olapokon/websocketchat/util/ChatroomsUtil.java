package olapokon.websocketchat.util;

import java.util.regex.Pattern;

public final class ChatroomsUtil {

    private ChatroomsUtil() {}

    public static boolean isInvalidChatroomName(String name) {
        return name.length() > 20
                || Pattern.compile("[^a-zA-Z0-9\s]")
                .matcher(name)
                .find();
    }
}
