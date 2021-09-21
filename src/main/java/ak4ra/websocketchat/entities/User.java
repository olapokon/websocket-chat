package ak4ra.websocketchat.entities;

import java.util.HashSet;
import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Represents a user of the chat application.
 * <p>
 * Only github login is currently supported, so every user is expected to have a github id and login/username.
 */
@Document
public class User {

    @Id
    private String id;

    @Indexed(unique = true, partialFilter = "{ githubId: { $exists: true } }")
    private String githubId;

    private String githubLogin;

    /**
     * The {@link Chatroom}s to which the user has access.
     */
    @DBRef(db = "Chatroom", lazy = true)
    private Set<Chatroom> chatrooms = new HashSet<>();

    public User() {}

    public User(String githubId, String githubLogin) {
        this.githubId = githubId;
        this.githubLogin = githubLogin;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public Set<Chatroom> getChatrooms() {
        return chatrooms;
    }

    public void setChatrooms(Set<Chatroom> chatrooms) {
        this.chatrooms = chatrooms;
    }

    @Override
    public String toString() {
        return "User{" +
               "id='" + id + '\'' +
               ", githubId='" + githubId + '\'' +
               ", githubLogin='" + githubLogin + '\'' +
               ", chatrooms=" + chatrooms +
               '}';
    }
}
