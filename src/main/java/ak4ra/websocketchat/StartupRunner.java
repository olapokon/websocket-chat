package ak4ra.websocketchat;

import ak4ra.websocketchat.chatroom.ChatroomRepository;
import ak4ra.websocketchat.entities.Chatroom;
import ak4ra.websocketchat.entities.User;
import ak4ra.websocketchat.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class StartupRunner implements CommandLineRunner {

    private final ChatroomRepository chatroomRepository;
    private final UserRepository     userRepository;

    @Autowired
    public StartupRunner(ChatroomRepository chatroomRepository,
                         UserRepository userRepository) {
        this.chatroomRepository = chatroomRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) {
        User u = new User("githubId1", "githubLogin1");
        userRepository.insert(u);

        Chatroom c = new Chatroom();
        c.setName("default chatroom 1");
        c.setEndpoint("/default-1");
        c.getAuthorizedUsers().add(u);
        chatroomRepository.insert(c);
    }
}
