package ak4ra.websocketchat.user;

import java.util.Optional;

import ak4ra.websocketchat.entities.User;
import ak4ra.websocketchat.entities.UserType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findUserByProvidedIdAndType(String id, UserType type);

    @Query(value = "SELECT u FROM User u LEFT JOIN FETCH u.accessibleChatrooms WHERE u.providedId = :id AND u.type = :type")
    Optional<User> getUserByProvidedIdAndTypeAndFetchAccessibleChatrooms(@Param("id") String id, @Param("type") UserType type);
}
