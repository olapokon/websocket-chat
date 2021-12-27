package olapokon.websocketchat.entities;

import java.util.Objects;

/**
 * Represents a user of the chat application.
 * <p>
 * Only github login is currently supported, so every user is expected to have a github id and login/username.
 */
public class User {

    /**
     * The user's identity provider.
     * <p>
     * Only GitHub is currently supported.
     */
    private UserType type;

    /**
     * The user's id returned by the identity provider.
     */
    private String providedId;

    private String username;

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

    @Override
    public String toString() {
        return "User{" +
               "type=" + type +
               ", providedId='" + providedId + '\'' +
               ", username='" + username + '\'' +
               '}';
    }
}
