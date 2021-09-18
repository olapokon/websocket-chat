package ak4ra.websocketchat.test;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TestWebController {

    @GetMapping("/")
    public String websocketTest(Model model) {
        model.addAttribute("A", "AAAAA");
        return "stomp-test";
    }
}
