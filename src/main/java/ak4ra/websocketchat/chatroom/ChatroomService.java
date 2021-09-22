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

    public List<Chatroom> findAllChatrooms() {
        return chatroomRepository.findAll();
    }

    /**
     * Inserts a chatroom into the database if it does not exist, otherwise finds and returns it.
     *
     * @param c
     *         the chatroom to get or save to the database
     *
     * @return the chatroom
     */
    public Chatroom getOrCreateChatroom(Chatroom c) {
        if (c.getName() == null || c.getName().isBlank()) {
            throw new ValidationException("Chatroom name cannot be empty");
        }
        if (c.getEndpoint() == null || c.getEndpoint().isBlank()) {
            throw new ValidationException("Chatroom endpoint cannot be empty");
        }
        return chatroomRepository.findChatroomByName(c.getName()).orElseGet(() -> chatroomRepository.save(c));
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
