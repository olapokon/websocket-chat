package ak4ra.websocketchat.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Represents a user of the chat application.
 * <p>
 * Only github login is currently supported, so every user is expected to have a github id and login/username.
 */
@Entity
@IdClass(UserId.class)
@Table(name = "user")
public class User {

    /**
     * The user's identity provider.
     * <p>
     * Only GitHub is currently supported.
     */
    @Id
    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private UserType type;

    /**
     * The user's id returned by the identity provider.
     */
    @Id
    @Column(nullable = false)
    private String providedId;

    @Column(nullable = false)
    private String username;

    /**
     * The {@link Chatroom}s to which the user is currently in.
     */
    @ManyToMany(mappedBy = "activeUsers")
    private Set<Chatroom> activeChatrooms = new HashSet<>();

    public User() {}

    public User(UserType type, String providedId, String username) {
        this.type = type;
        this.providedId = providedId;
        this.username = username;
    }

    public UserType getType() {
        return type;
    }

    public void setType(UserType type) {
        this.type = type;
    }

    public String getProvidedId() {
        return providedId;
    }

    public void setProvidedId(String providedId) {
        this.providedId = providedId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Set<Chatroom> getActiveChatrooms() {
        return activeChatrooms;
    }

    public void setActiveChatrooms(Set<Chatroom> activeChatrooms) {
        this.activeChatrooms = activeChatrooms;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        User user = (User) o;
        return type == user.type && providedId.equals(user.providedId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, providedId);
    }
}
