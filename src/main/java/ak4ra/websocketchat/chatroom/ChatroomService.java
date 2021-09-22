package ak4ra.websocketchat.chatroom;

import java.util.List;

import ak4ra.websocketchat.entities.Chatroom;
import ak4ra.websocketchat.entities.User;
import ak4ra.websocketchat.exceptions.ResourceNotFoundException;
import ak4ra.websocketchat.exceptions.ValidationException;
import ak4ra.websocketchat.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChatroomService {

    private final ChatroomRepository chatroomRepository;
    private final UserRepository     userRepository;

    @Autowired
    public ChatroomService(ChatroomRepository chatroomRepository, UserRepository userRepository) {
        this.chatroomRepository = chatroomRepository;
        this.userRepository = userRepository;
    }

    public List<Chatroom> getChatrooms() {
        return chatroomRepository.findAll();
    }

    // TODO: transactions
    public void addAuthorizedUserToChatroom(User user, String chatroomId) {
        Chatroom c = chatroomRepository.findById(chatroomId)
                                       .orElseThrow(() -> new ResourceNotFoundException("Chatroom not found."));
        if (user.getId() == null) {
            throw new ValidationException("User id cannot be null.");
        }

        User u = userRepository.findById(user.getId()).orElseGet(() -> userRepository.save(user));
        c.getAuthorizedUsers().add(u);
        chatroomRepository.save(c);
    }

    public void addActiveUserToChatroom(User user, String chatroomId) {
        Chatroom c = chatroomRepository.findById(chatroomId)
                                       .orElseThrow(() -> new ResourceNotFoundException("Chatroom not found."));
        if (user.getId() == null) {
            throw new ValidationException("User id cannot be null.");
        }

        User u = userRepository.findById(user.getId()).orElseGet(() -> userRepository.save(user));
        c.getActiveUsers().add(u);
        chatroomRepository.save(c);
    }
}
