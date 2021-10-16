package olapokon.websocketchat.chatroom;

import java.util.List;

import olapokon.websocketchat.messages.MessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/chat")
public class ChatroomController {

    private final Logger log = LoggerFactory.getLogger(ChatroomController.class);

    private static final String WEBSOCKET_URL = "ws://localhost:8080/ws";

    /**
     * Subscribe destination for STOMP frames. Additional segments can be appended.
     * <p>
     * (can subscript to all with "topic/all:
     */
    private static final String SUBSCRIBE_DESTINATION = "/topic";

    /**
     * Destination for STOMP frames. Additional segments can be appended.
     */
    private static final String PUBLISH_DESTINATION = "/app/ws-chat";

    private final ChatroomService chatroomService;
    private final MessageService  messageService;

    @Autowired
    public ChatroomController(ChatroomService chatroomService,
                              MessageService messageService) {
        this.chatroomService = chatroomService;
        this.messageService = messageService;
    }

    /**
     * Returns a webpage with the list of chatrooms.
     */
    @GetMapping("/list")
    public String chatroomList(Model model) {
        model.addAttribute("chatrooms", chatroomService.findAllChatrooms());
        return "chatroom-list";
    }

    /**
     * Returns a {@link List} with the usernames of the users currently connected to the provided chatroom destination.
     */
    @ResponseBody
    @GetMapping("/{destination}/user-list")
    public List<String> getActiveUsersList(@PathVariable String destination) {
        return chatroomService
                .getActiveUsersList(SUBSCRIBE_DESTINATION + "/" + destination);
    }

    /**
     * Sends a chatroom webpage that initiated a websocket connection to the corresponding topic.
     *
     * @param chatroomId
     *         the id of the chatroom for which a page is requested
     * @param model
     *
     * @return
     */
    // TODO: make protected
    @GetMapping("/room/{chatroomId}")
    @PostAuthorize("hasPermission(#chatroomId, null)")
    public String chatroom(@PathVariable String chatroomId, Model model) {
        model.addAttribute("chatroomId", chatroomId);
        model.addAttribute("WEBSOCKET_URL", WEBSOCKET_URL);
        model.addAttribute("SUBSCRIBE_DESTINATION", SUBSCRIBE_DESTINATION + "/" + chatroomId);
        model.addAttribute("PUBLISH_DESTINATION", PUBLISH_DESTINATION + "/" + chatroomId);
        return "chatroom";
    }
}
