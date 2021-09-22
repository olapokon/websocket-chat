package ak4ra.websocketchat.user;

import java.util.List;

import ak4ra.websocketchat.entities.User;
import ak4ra.websocketchat.exceptions.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Gets or creates a user authenticated through github. Should have a githubId.
     *
     * @param user
     *         the user to get or create
     *
     * @return the user
     */
    public User getOrCreateGithubUser(User user) {
        if (user.getGithubId() == null) {
            throw new ValidationException("Github user must have a github id.");
        }
        return userRepository.findUserByGithubId(user.getGithubId()).orElseGet(() -> userRepository.save(user));
    }
}
