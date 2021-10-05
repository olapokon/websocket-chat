package olapokon.websocketchat.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.authentication.event.LogoutSuccessEvent;
import org.springframework.stereotype.Component;

/**
 * Listens for login and logout events. Only for debugging.
 */
@Component
public class LoginLogoutListener {

    private static final Logger log = LoggerFactory.getLogger(LoginLogoutListener.class);

    @EventListener
    public void successfulLogin(AuthenticationSuccessEvent e) {
        log.debug("User {} logged in", e.getAuthentication().getName());
    }

    @EventListener
    public void logout(LogoutSuccessEvent e) {
        log.debug("User {} logged out", e.getAuthentication().getName());
    }
}
