package ak4ra.websocketchat.controllers;

import ak4ra.websocketchat.domain.Chatroom;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/chat")
public class ChatroomController {

    private static final String WEBSOCKET_URL         = "ws://localhost:8080/ws";
    private static final String SUBSCRIBE_DESTINATION = "/topic/all";

    /**
     * Destination for the STOMP messages. Additional segments can be appended.
     */
    private static final String PUBLISH_DESTINATION = "/app/ws-test";

    /**
     * Chatrooms available by default.
     */
    // TODO: move elsewhere
    private static final Chatroom[] DEFAULT_CHATROOMS = {
            new Chatroom("default chatroom 1", "/default-1"),
            new Chatroom("default chatroom 2", "/default-2"),
            new Chatroom("default chatroom 3", "/default-3"),
            new Chatroom("default chatroom 4", "/default-4"),
            };

    // TODO: javadoc
    @GetMapping("/list")
    public String chatroomList(Model model) {
        model.addAttribute("chatrooms", DEFAULT_CHATROOMS);
        return "chatroom-list";
    }

    // TODO: javadoc
    @GetMapping("/room/{chatroomId}")
    @PostAuthorize("hasPermission(#chatroomId, null)")
    public String chatroom(@PathVariable String chatroomId, Model model) {
        model.addAttribute("chatroomId", chatroomId);

        model.addAttribute("WEBSOCKET_URL", WEBSOCKET_URL);
        model.addAttribute("SUBSCRIBE_DESTINATION", SUBSCRIBE_DESTINATION);
        model.addAttribute("PUBLISH_DESTINATION", PUBLISH_DESTINATION + "/" + chatroomId);
        return "chatroom";
    }
}
