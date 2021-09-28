package ak4ra.websocketchat.chatroom;

import java.util.List;
import java.util.Set;

import ak4ra.websocketchat.entities.Chatroom;
import ak4ra.websocketchat.entities.User;
import ak4ra.websocketchat.exceptions.ResourceNotFoundException;
import ak4ra.websocketchat.exceptions.ValidationException;
import ak4ra.websocketchat.presence.UserPresenceTracker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ChatroomService {

    private static final String CHATROOM_NOT_FOUND = "Chatroom not found.";

    private final ChatroomRepository  chatroomRepository;
    private final UserPresenceTracker userPresenceTracker;

    @Autowired
    public ChatroomService(ChatroomRepository chatroomRepository, UserPresenceTracker userPresenceTracker) {
        this.chatroomRepository = chatroomRepository;
        this.userPresenceTracker = userPresenceTracker;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<Chatroom> findAllChatrooms() {
        return chatroomRepository.findAll();
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public Chatroom createChatroom(Chatroom c) {
        if (c.getName() == null || c.getName().isBlank())
            throw new ValidationException("Chatroom name cannot be empty");
        if (c.getEndpoint() == null || c.getEndpoint().isBlank())
            throw new ValidationException("Chatroom endpoint cannot be empty");
        return chatroomRepository.save(c);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void addAuthorizedUserToChatroom(User user, Long chatroomId) {
        Chatroom c = chatroomRepository
                .findById(chatroomId)
                .orElseThrow(() -> new ResourceNotFoundException(CHATROOM_NOT_FOUND));
        c.getAuthorizedUsers().add(user);
        chatroomRepository.save(c);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public Set<User> getAuthorizedUsers(Long chatroomId) {
        return chatroomRepository
                .getChatroomByIdAndFetchAuthorizedUsers(chatroomId)
                .orElseThrow(() -> new ResourceNotFoundException(CHATROOM_NOT_FOUND))
                .getAuthorizedUsers();
    }

    public Set<User> getActiveUsers(Long chatroomId) {
        Chatroom c = chatroomRepository
                .findById(chatroomId)
                .orElseThrow(() -> new ResourceNotFoundException(CHATROOM_NOT_FOUND));
        return userPresenceTracker.getUserList(c);
    }
}
