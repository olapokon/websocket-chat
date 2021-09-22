package ak4ra.websocketchat.entities;

import java.util.HashSet;
import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Represents a chatroom.
 */
@Document
public class Chatroom {

    @Id
    private String id;

    /**
     * The name of the chatroom.
     * <p>
     * Chatroom names are unique.
     */
    @Indexed(unique = true)
    private String name;

    /**
     * To be appended to the message broker destination.
     */
    private String endpoint;

    /**
     * The {@link User}s who are authorized to access the chatroom.
     */
    @DBRef(db = "User", lazy = true)
    private Set<User> authorizedUsers = new HashSet<>();

    /**
     * The {@link User}s who are currently the chatroom.
     */
    @DBRef(db = "User", lazy = true)
    private Set<User> activeUsers = new HashSet<>();

    public Chatroom() {}

    public Chatroom(String name, String endpoint) {
        this.name = name;
        this.endpoint = endpoint;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public Set<User> getAuthorizedUsers() {
        return authorizedUsers;
    }

    public void setAuthorizedUsers(Set<User> authorizedUsers) {
        this.authorizedUsers = authorizedUsers;
    }

    public Set<User> getActiveUsers() {
        return activeUsers;
    }

    public void setActiveUsers(Set<User> activeUsers) {
        this.activeUsers = activeUsers;
    }

    @Override
    public String toString() {
        return "Chatroom{" +
               "id='" + id + '\'' +
               ", name='" + name + '\'' +
               ", endpoint='" + endpoint + '\'' +
               ", authorizedUsers=" + authorizedUsers +
               ", activeUsers=" + activeUsers +
               '}';
    }
}
