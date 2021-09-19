package ak4ra.websocketchat.chatroom;

import ak4ra.websocketchat.test.MainWebsocketHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/chat")
public class ChatroomController {

    private final Logger log = LoggerFactory.getLogger(MainWebsocketHandler.class);

    private static final String WEBSOCKET_URL = "ws://localhost:8080/ws";

    /**
     * Subscribe destination for the STOMP messages. Additional segments can be appended.
     * <p>
     * (can subscript to all with "topic/all:
     */
    private static final String SUBSCRIBE_DESTINATION = "/topic";

    /**
     * Destination for the STOMP messages. Additional segments can be appended.
     */
    private static final String PUBLISH_DESTINATION = "/app/ws-chat";

    private final ChatroomService chatroomService;

    @Autowired
    public ChatroomController(ChatroomService chatroomService) {
        this.chatroomService = chatroomService;
    }

    // TODO: javadoc
    @GetMapping("/list")
    public String chatroomList(Model model) {
        model.addAttribute("chatrooms", chatroomService.getChatrooms());
        return "chatroom-list";
    }

    // TODO: javadoc
    @GetMapping("/room/{chatroomId}")
    @PostAuthorize("hasPermission(#chatroomId, null)")
    public String chatroom(@PathVariable String chatroomId, Model model) {
        DefaultOAuth2User ud
                = (DefaultOAuth2User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.info("user: {}", ud);

        model.addAttribute("chatroomId", chatroomId);
        model.addAttribute("WEBSOCKET_URL", WEBSOCKET_URL);
        //        model.addAttribute("SUBSCRIBE_DESTINATION", SUBSCRIBE_DESTINATION);
        model.addAttribute("SUBSCRIBE_DESTINATION", SUBSCRIBE_DESTINATION + "/" + chatroomId);
        model.addAttribute("PUBLISH_DESTINATION", PUBLISH_DESTINATION + "/" + chatroomId);
        return "chatroom";
    }
}
