package ak4ra.websocketchat;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import ak4ra.websocketchat.chatroom.ChatroomRepository;
import ak4ra.websocketchat.entities.Chatroom;
import ak4ra.websocketchat.entities.User;
import ak4ra.websocketchat.user.UserRepository;
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

    private static final List<User>     DEFAULT_USERS     = new ArrayList<>();
    private static final List<Chatroom> DEFAULT_CHATROOMS = new ArrayList<>();

    static {
        User u = new User("githubId1", "githubLogin1");

        DEFAULT_USERS.add(u);

        Chatroom c1 = new Chatroom();
        c1.setName("default chatroom 1");
        c1.setEndpoint("/default-1");
        c1.setAuthorizedUsers(Set.of());
        c1.setActiveUsers(Set.of());

        Chatroom c2 = new Chatroom();
        c2.setName("default chatroom 2");
        c2.setEndpoint("/default-2");
        c2.setAuthorizedUsers(Set.of());
        c2.setActiveUsers(Set.of());

        Chatroom c3 = new Chatroom();
        c3.setName("default chatroom 3");
        c3.setEndpoint("/default-3");
        c3.setAuthorizedUsers(Set.of());
        c3.setActiveUsers(Set.of());

        Chatroom c4 = new Chatroom();
        c4.setName("default chatroom 4");
        c4.setEndpoint("/default-4");
        c4.setAuthorizedUsers(Set.of());
        c4.setActiveUsers(Set.of());

        DEFAULT_CHATROOMS.addAll(List.of(c1, c2, c3, c4));
    }

    @Autowired
    public StartupRunner(ChatroomRepository chatroomRepository,
                         UserRepository userRepository) {
        this.chatroomRepository = chatroomRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) {
        DEFAULT_USERS.forEach(u -> log.info("{}", userRepository
                .findUserByGithubId(u.getGithubId()).orElseGet(() -> userRepository.save(u))));

        DEFAULT_CHATROOMS.forEach(c -> log.info("{}", chatroomRepository
                .findChatroomByName(c.getName()).orElseGet(() -> chatroomRepository.save(c))));
    }
}
