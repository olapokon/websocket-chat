package olapokon.websocketchat.chatroom;

import java.util.List;

import olapokon.websocketchat.ApplicationProperties;
import olapokon.websocketchat.util.ChatroomUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/chat")
public class ChatroomController {

    private final ApplicationProperties applicationProperties;

    /**
     * Subscribe destination for STOMP frames. Additional segments can be appended.
     * <p>
     * Can subscribe to all with "topic/all
     */
    private static final String SUBSCRIBE_DESTINATION = "/topic";

    /**
     * Destination for STOMP frames. Additional segments can be appended.
     */
    private static final String PUBLISH_DESTINATION = "/app/ws-chat";

    private final ChatroomService chatroomService;

    @Autowired
    public ChatroomController(ApplicationProperties applicationProperties, ChatroomService chatroomService) {
        this.applicationProperties = applicationProperties;
        this.chatroomService = chatroomService;
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
     * @param chatroomName the id of the chatroom for which a page is requested
     * @param model
     * @return
     */
    @GetMapping("/room/{chatroomName}")
    public String chatroom(@PathVariable String chatroomName, Model model) {
        final String name = chatroomName.trim();
        if (ChatroomUtil.isInvalidChatroomName(name)) {
            return "home";
        }
        model.addAttribute("chatroomId", name);
        model.addAttribute("WEBSOCKET_URL", applicationProperties.getWebsocketUrl());
        model.addAttribute("SUBSCRIBE_DESTINATION", SUBSCRIBE_DESTINATION + "/" + name);
        model.addAttribute("PUBLISH_DESTINATION", PUBLISH_DESTINATION + "/" + name);
        return "chatroom";
    }
}
