package olapokon.websocketchat.entities;

import java.io.Serializable;
import java.util.Objects;

/**
 * Composite primary key for the user table
 */
public class UserId implements Serializable {

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

    public UserId() {}

    public UserId(UserType type, String providedId) {
        this.type = type;
        this.providedId = providedId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserId userId = (UserId) o;
        return type == userId.type && Objects.equals(providedId, userId.providedId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, providedId);
    }
}
