package ak4ra.websocketchat.user;

import java.util.List;

import ak4ra.websocketchat.entities.User;
import ak4ra.websocketchat.exceptions.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<User> findAllUsers() {
        return userRepository.findAll();
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
        if (user.getType() == null) {
            throw new ValidationException("Github user must have a type.");
        }
        if (user.getProvidedId() == null) {
            throw new ValidationException("Github user must have a providedId.");
        }
        if (user.getUsername() == null) {
            throw new ValidationException("Github user must have a username.");
        }
        return userRepository.save(user);
    }
}
