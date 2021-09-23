package ak4ra.websocketchat.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

// TODO: https://docs.spring.io/spring-security/site/docs/current/reference/html5/#servlet-authentication
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    // TODO: openid?
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
            // TODO: change websocket authenticated routes?
            .antMatchers("/ws", "/chat/room/**")
            .authenticated()
            .anyRequest()
            .permitAll()
            .and()
            .oauth2Login()
            .loginPage("/login");
    }
}
