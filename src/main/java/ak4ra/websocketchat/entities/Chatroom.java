package ak4ra.websocketchat.entities;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents a chatroom.
 */
@Entity
@Table(name = "chatroom")
public class Chatroom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
     */
    @OneToMany(cascade = CascadeType.ALL)
    private Set<User> authorizedUsers = new HashSet<>();

    /**
     * The {@link User}s who are currently the chatroom.
     */
    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    //    @ManyToMany
    @JoinTable(name = "chatroom_active_user",
               joinColumns = @JoinColumn(name = "chatroom"),
               inverseJoinColumns = @JoinColumn(name = "active_user"))
    private Set<User> activeUsers = new HashSet<>();

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

    public Set<User> getActiveUsers() {
        return activeUsers;
    }

    public void setActiveUsers(Set<User> activeUsers) {
        this.activeUsers = activeUsers;
    }
}
