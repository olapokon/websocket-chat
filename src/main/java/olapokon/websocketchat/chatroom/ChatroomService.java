package olapokon.websocketchat.chatroom;

import java.util.List;
import java.util.Set;

import olapokon.websocketchat.entities.Chatroom;
import olapokon.websocketchat.entities.User;
import olapokon.websocketchat.exceptions.ResourceNotFoundException;
import olapokon.websocketchat.exceptions.ValidationException;
import olapokon.websocketchat.presence.UserPresenceTracker;
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

    // TODO: remove - unimplemented/will not be implemented?
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<Chatroom> findAllChatrooms() {
        return chatroomRepository.findAll();
    }

    // TODO: remove - unimplemented/will not be implemented?
    @Transactional(propagation = Propagation.REQUIRED)
    public Chatroom createChatroom(Chatroom c) {
        if (c.getName() == null || c.getName().isBlank())
            throw new ValidationException("Chatroom name cannot be empty");
        if (c.getEndpoint() == null || c.getEndpoint().isBlank())
            throw new ValidationException("Chatroom endpoint cannot be empty");
        return chatroomRepository.save(c);
    }

    // TODO: remove - unimplemented/will not be implemented?
    @Transactional(propagation = Propagation.REQUIRED)
    public void addAuthorizedUserToChatroom(User user, Long chatroomId) {
        Chatroom c = chatroomRepository
                .findById(chatroomId)
                .orElseThrow(() -> new ResourceNotFoundException(CHATROOM_NOT_FOUND));
        c.getAuthorizedUsers().add(user);
        chatroomRepository.save(c);
    }

    // TODO: remove - unimplemented/will not be implemented?
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public Set<User> getAuthorizedUsers(Long chatroomId) {
        return chatroomRepository
                .getChatroomByIdAndFetchAuthorizedUsers(chatroomId)
                .orElseThrow(() -> new ResourceNotFoundException(CHATROOM_NOT_FOUND))
                .getAuthorizedUsers();
    }

    /**
     * Returns the usernames of the users currently connected to the chatroom.
     */
    public List<String> getActiveUsersList(String destination) {
        return userPresenceTracker.getUserList(destination)
                                  .stream()
                                  .map(User::getUsername)
                                  .toList();
    }
}
