package olapokon.websocketchat.entities;

import java.util.HashSet;
import java.util.Set;

/**
 * Represents a chatroom.
 */
public class Chatroom {

    private Long id;

    /**
     * The name of the chatroom.
     * <p>
     * Chatroom names are unique.
     */
    private String name;

    /**
     * To be appended to the message broker destination.
     */
    private String endpoint;

    /**
     * The {@link User}s who are authorized to access the chatroom.
     * <p>
     * Bi-directional many-to-many relationship.
     */
    private Set<User> authorizedUsers = new HashSet<>();

    public Chatroom() {}

    public Chatroom(String name, String endpoint) {
        this.name = name;
        this.endpoint = endpoint;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    @Override
    public String toString() {
        return "Chatroom{" +
               "id=" + id +
               ", name='" + name + '\'' +
               ", endpoint='" + endpoint + '\'' +
               '}';
    }
}
