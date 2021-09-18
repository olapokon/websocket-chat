package ak4ra.websocketchat.security;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {
    private static final String GITHUB_LOGIN_URL = "/oauth2/authorization/github";

    @GetMapping("/login")
    public String loginPage(Model model) {
        model.addAttribute("GITHUB_LOGIN_URL", GITHUB_LOGIN_URL);
        return "login";
    }
}
