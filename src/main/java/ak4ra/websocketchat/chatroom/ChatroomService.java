package ak4ra.websocketchat.chatroom;

import java.util.List;

import ak4ra.websocketchat.entities.Chatroom;
import ak4ra.websocketchat.entities.User;
import ak4ra.websocketchat.exceptions.ResourceNotFoundException;
import ak4ra.websocketchat.exceptions.ValidationException;
import ak4ra.websocketchat.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ChatroomService {

    private final ChatroomRepository chatroomRepository;
    private final UserRepository     userRepository;

    @Autowired
    public ChatroomService(ChatroomRepository chatroomRepository, UserRepository userRepository) {
        this.chatroomRepository = chatroomRepository;
        this.userRepository = userRepository;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<Chatroom> findAllChatrooms() {
        return chatroomRepository.findAll();
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public Chatroom createChatroom(Chatroom c) {
        if (c.getName() == null || c.getName().isBlank()) {
            throw new ValidationException("Chatroom name cannot be empty");
        }
        if (c.getEndpoint() == null || c.getEndpoint().isBlank()) {
            throw new ValidationException("Chatroom endpoint cannot be empty");
        }
        return chatroomRepository.save(c);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void addAuthorizedUserToChatroom(User user, Long chatroomId) {
        Chatroom c = chatroomRepository
                .findById(chatroomId)
                .orElseThrow(() -> new ResourceNotFoundException("Chatroom not found."));
        c.getAuthorizedUsers().add(user);
        chatroomRepository.save(c);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void addActiveUserToChatroom(User user, Long chatroomId) {
        Chatroom c = chatroomRepository.findById(chatroomId)
                                       .orElseThrow(() -> new ResourceNotFoundException("Chatroom not found."));
        c.getActiveUsers().add(user);
        chatroomRepository.save(c);
    }
}
