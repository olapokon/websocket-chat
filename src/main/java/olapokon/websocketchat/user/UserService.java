package olapokon.websocketchat.user;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import olapokon.websocketchat.entities.Chatroom;
import olapokon.websocketchat.entities.User;
import olapokon.websocketchat.entities.UserType;
import olapokon.websocketchat.exceptions.ResourceNotFoundException;
import olapokon.websocketchat.exceptions.ValidationException;
import olapokon.websocketchat.messages.MessageService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private static final String USER_NOT_FOUND = "User not found.";

    private final UserRepository userRepository;
    private final MessageService messageService;

    @Autowired
    public UserService(UserRepository userRepository,
                       MessageService messageService) {
        this.userRepository = userRepository;
        this.messageService = messageService;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public Optional<User> findUserById(String providedId, UserType type) {
        return userRepository.findUserByProvidedIdAndType(providedId, type);
    }

    /**
     * Gets all the chatrooms the user is currently in or throws.
     *
     * @param providedId
     *         the user's providedId
     * @param type
     *         the user's {@link UserType}
     *
     * @return the set of chatrooms
     */
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public Set<Chatroom> getAccessibleChatrooms(String providedId, UserType type) {
        return userRepository
                .getUserByProvidedIdAndTypeAndFetchAccessibleChatrooms(providedId, type)
                .orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND))
                .getAccessibleChatrooms();
    }

    /**
     * Creates a user. Should have a type, providedId, and username.
     *
     * @param user
     *         the user to get or create
     *
     * @return the created user
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public User createUser(User user) {
        if (user.getType() == null)
            throw new ValidationException("Github user must have a type.");
        if (user.getProvidedId() == null)
            throw new ValidationException("Github user must have a providedId.");
        if (user.getUsername() == null)
            throw new ValidationException("Github user must have a username.");
        return userRepository.save(user);
    }

    /**
     * Handles the subscription of a user to a chatroom.
     *
     * @param user
     *         the user that is connecting to the chatroom
     * @param destination
     *         the chatroom's destination in the message broker
     *
     * @throws JsonProcessingException
     */
    public void userJoinChatroom(User user, String destination) throws JsonProcessingException {
        messageService.sendUserJoinedMessage(destination, user.getUsername());
        messageService.sendUserListUpdate(destination);
    }

    /**
     * Handles a user's disconnection from a chatroom.
     *
     * @param user
     *         the user that is disconnecting
     * @param destination
     *         the chatroom's destination in the message broker
     *
     * @throws JsonProcessingException
     */
    public void userLeaveChatroom(User user, String destination) throws JsonProcessingException {
        messageService.sendUserLeftMessage(destination, user.getUsername());
        messageService.sendUserListUpdate(destination);
    }
}
