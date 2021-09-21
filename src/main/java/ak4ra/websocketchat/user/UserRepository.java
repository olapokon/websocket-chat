package ak4ra.websocketchat.user;

import javax.swing.text.html.Option;

import java.util.Optional;

import ak4ra.websocketchat.entities.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<User, String> {

    Optional<User> findUserByGithubId(String githubId);
}
