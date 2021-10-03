package ak4ra.websocketchat;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import ak4ra.websocketchat.chatroom.ChatroomRepository;
import ak4ra.websocketchat.chatroom.ChatroomService;
import ak4ra.websocketchat.entities.Chatroom;
import ak4ra.websocketchat.entities.User;
import ak4ra.websocketchat.entities.UserType;
import ak4ra.websocketchat.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Loads some data into the database.
 */
@Component
public class StartupRunner implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(StartupRunner.class);

    private final ChatroomRepository chatroomRepository;
    private final ChatroomService    chatroomService;
    private final UserService        userService;

    private static final List<User>     DEFAULT_USERS     = new ArrayList<>();
    private static final List<Chatroom> DEFAULT_CHATROOMS = new ArrayList<>();

    static {
        User u = new User(UserType.GITHUB, "githubId1", "githubLogin1");
        User u1 = new User(UserType.GITHUB, "githubId2", "githubLogin2");
        User u2 = new User(UserType.GITHUB, "githubId3", "githubLogin3");
        DEFAULT_USERS.addAll(List.of(u, u1, u2));

        Chatroom c1 = new Chatroom("default chatroom 1", "/default-1");
        Chatroom c2 = new Chatroom("default chatroom 2", "/default-2");
        Chatroom c3 = new Chatroom("default chatroom 3", "/default-3");
        Chatroom c4 = new Chatroom("default chatroom 4", "/default-4");
        DEFAULT_CHATROOMS.addAll(List.of(c1, c2, c3, c4));
    }

    public StartupRunner(ChatroomRepository chatroomRepository,
                         ChatroomService chatroomService, UserService userService) {
        this.chatroomRepository = chatroomRepository;
        this.chatroomService = chatroomService;
        this.userService = userService;
    }

    @Override
    public void run(String... args) {
        DEFAULT_USERS.forEach(userService::createUser);
        //        DEFAULT_CHATROOMS.forEach(chatroomService::createChatroom);

        //        Set<Chatroom> chatrooms = userService.getActiveChatrooms("githubId1", UserType.GITHUB);
        //        log.info("user active chatrooms: {}", chatrooms);
        //        Set<Chatroom> chatrooms1 = userService.getActiveChatrooms("githubId3", UserType.GITHUB);
        //        log.info("user active chatrooms: {}", chatrooms1);
        //
        //        Set<User> users = chatroomService.getAuthorizedUsers(8L);
        //        log.info("chatroom.getAuthorizedUsers: {}", users);
        //        Set<User> users1 = chatroomService.getActiveUsers(8L);
        //        log.info("chatroom.getActiveUsers: {}", users1);
        //        Set<User> users2 = chatroomService.getAuthorizedUsers(4L);
        //        log.info("chatroom.getAuthorizedUsers: {}", users2);
        //        Set<User> users3 = chatroomService.getActiveUsers(5L);
        //        log.info("chatroom.getActiveUsers: {}", users3);

        chatroomService.addAuthorizedUserToChatroom(new User(UserType.GITHUB, "githubId14", "githubLogin14"), 1L);
    }
}
