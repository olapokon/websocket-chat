package olapokon.websocketchat.util;

import java.time.ZonedDateTime;
import java.util.regex.Pattern;

public final class ChatroomUtil {

    private ChatroomUtil() {}

    public static boolean isInvalidChatroomName(String name) {
        return name.length() > 20
                || Pattern.compile("[^a-zA-Z0-9\s]")
                .matcher(name)
                .find();
    }

    public static String createTimestamp() {
        return ZonedDateTime.now().toString();
    }
}
