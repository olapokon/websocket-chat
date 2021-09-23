package ak4ra.websocketchat;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import ak4ra.websocketchat.chatroom.ChatroomRepository;
import ak4ra.websocketchat.chatroom.ChatroomService;
import ak4ra.websocketchat.entities.Chatroom;
import ak4ra.websocketchat.entities.User;
import ak4ra.websocketchat.user.UserRepository;
import ak4ra.websocketchat.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
        User u = new User("githubId1", "githubLogin1");
        DEFAULT_USERS.add(u);

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
        //        DEFAULT_USERS.forEach(u -> log.info("{}", userService.getOrCreateGithubUser(u)));

        //        DEFAULT_CHATROOMS.forEach(c -> log.info("{}", chatroomService.getOrCreateChatroom(c)));

        //        var users = userService.findAllUsers();
        //        log.info("users: {}", users);
        //        log.info("findAllUsers(): {}", users
        //                .stream()
        //                .map(u -> "\n" + u.toString())
        //                .collect(Collectors.joining()));
        //        log.info("findAllChatrooms(): {}", chatroomService
        //                .findAllChatrooms()
        //                .stream()
        //                .map(c -> "\n" + c.toString())
        //                .collect(Collectors.joining()));

        //        User u2 = new User("githubId2", "githubLogin2");
        //        chatroomService.addAuthorizedUserToChatroom(u2, 1L);
        //        User u3 = new User("githubId2", "githubLogin2");
        //        chatroomService.addActiveUserToChatroom(u3, 1L);
        //        var chatroom = chatroomRepository.getById(1L);
        //        chatroom.getAuthorizedUsers().add(u2);
        //        chatroomRepository.save(chatroom);
        //        log.info("getOrCreateGithubUser: {}", userService.getOrCreateGithubUser(u2));

        //        chatroomService.transactionTest(DEFAULT_CHATROOMS.get(0));
        //        log.info("after transactionTest");
    }
}
