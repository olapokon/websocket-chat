package ak4ra.websocketchat.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents a user of the chat application.
 * <p>
 * Only github login is currently supported, so every user is expected to have a github id and login/username.
 */
@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String githubId;

    @Column(nullable = false)
    private String githubLogin;

    /**
     * The {@link Chatroom}s to which the user is currently in.
     */
    @ManyToMany(mappedBy = "activeUsers")
    private Set<Chatroom> activeChatrooms = new HashSet<>();

    public User() {}

    public User(String githubId, String githubLogin) {
        this.githubId = githubId;
        this.githubLogin = githubLogin;
    }

    public User(Long id, String githubId, String githubLogin,
                Set<Chatroom> chatrooms) {
        this.id = id;
        this.githubId = githubId;
        this.githubLogin = githubLogin;
        this.activeChatrooms = chatrooms;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGithubId() {
        return githubId;
    }

    public void setGithubId(String githubId) {
        this.githubId = githubId;
    }

    public String getGithubLogin() {
        return githubLogin;
    }

    public void setGithubLogin(String githubLogin) {
        this.githubLogin = githubLogin;
    }

    public Set<Chatroom> getActiveChatrooms() {
        return activeChatrooms;
    }

    public void setActiveChatrooms(Set<Chatroom> activeChatrooms) {
        this.activeChatrooms = activeChatrooms;
    }
}
