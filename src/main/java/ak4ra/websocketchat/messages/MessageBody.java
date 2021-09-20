package ak4ra.websocketchat.messages;

public record MessageBody(String username,
                          String message,
                          String timeStamp) {}
