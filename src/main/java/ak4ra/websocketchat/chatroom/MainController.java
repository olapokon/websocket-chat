package ak4ra.websocketchat.chatroom;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class MainController {

    @GetMapping("/")
    public RedirectView websocketTest(Model model) {
        return new RedirectView("/chat/list");
    }
}
