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
    private final UserRepository     userRepository;
    private final ChatroomService    chatroomService;
    private final UserService        userService;

    private static final List<User>     DEFAULT_USERS     = new ArrayList<>();
    private static final List<Chatroom> DEFAULT_CHATROOMS = new ArrayList<>();

    static {
        User u = new User("githubId1", "githubLogin1");

        DEFAULT_USERS.add(u);

        Chatroom c1 = new Chatroom();
        c1.setName("default chatroom 1");
        c1.setEndpoint("/default-1");
        c1.setAuthorizedUsers(new HashSet<>());
        c1.setActiveUsers(new HashSet<>());

        Chatroom c2 = new Chatroom();
        c2.setName("default chatroom 2");
        c2.setEndpoint("/default-2");
        c2.setAuthorizedUsers(new HashSet<>());
        c2.setActiveUsers(new HashSet<>());

        Chatroom c3 = new Chatroom();
        c3.setName("default chatroom 3");
        c3.setEndpoint("/default-3");
        c3.setAuthorizedUsers(new HashSet<>());
        c3.setActiveUsers(new HashSet<>());

        Chatroom c4 = new Chatroom();
        c4.setName("default chatroom 4");
        c4.setEndpoint("/default-4");
        c4.setAuthorizedUsers(new HashSet<>());
        c4.setActiveUsers(new HashSet<>());

        DEFAULT_CHATROOMS.addAll(List.of(c1, c2, c3, c4));
    }

    @Autowired
    public StartupRunner(ChatroomRepository chatroomRepository, UserRepository userRepository,
                         ChatroomService chatroomService, UserService userService) {
        this.chatroomRepository = chatroomRepository;
        this.userRepository = userRepository;
        this.chatroomService = chatroomService;
        this.userService = userService;
    }

    @Override
    public void run(String... args) {
        DEFAULT_USERS.forEach(u -> log.info("{}", userService.getOrCreateGithubUser(u)));

        DEFAULT_CHATROOMS.forEach(c -> log.info("{}", chatroomRepository
                .findChatroomByName(c.getName()).orElseGet(() -> chatroomRepository.save(c))));

        User u1 = userRepository.findUserByGithubId(DEFAULT_USERS.get(0).getGithubId()).orElseThrow();
        Chatroom c1 = chatroomRepository.findChatroomByName(DEFAULT_CHATROOMS.get(0).getName()).orElseThrow();
        chatroomService.addAuthorizedUserToChatroom(u1, c1.getId());

        log.info("userRepository.findAll(): {}", userRepository
                .findAll()
                .stream()
                .map(u -> "\n" + u.toString())
                .collect(Collectors.joining()));
        log.info("chatroomRepository.findAll(): {}", chatroomRepository
                .findAll()
                .stream()
                .map(c -> "\n" + c.toString())
                .collect(Collectors.joining()));

        User u2 = new User("githubId2", "githubLogin2");
        log.info("getOrCreateGithubUser: {}", userService.getOrCreateGithubUser(u2));
    }
}
