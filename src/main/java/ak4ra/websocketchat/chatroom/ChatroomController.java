package ak4ra.websocketchat.chatroom;

import ak4ra.websocketchat.messages.ChatroomEvent;
import ak4ra.websocketchat.messages.MessageService;
import com.fasterxml.jackson.core.JsonProcessingException;
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
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/chat")
public class ChatroomController {

    private final Logger log = LoggerFactory.getLogger(ChatroomController.class);

    private static final String WEBSOCKET_URL = "ws://localhost:8080/ws";

    /**
     * Subscribe destination for STOMP messages. Additional segments can be appended.
     * <p>
     * (can subscript to all with "topic/all:
     */
    private static final String SUBSCRIBE_DESTINATION = "/topic";

    /**
     * Destination for STOMP messages. Additional segments can be appended.
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
     *
     * @param model
     *
     * @return
     */
    @GetMapping("/list")
    public String chatroomList(Model model) {
        model.addAttribute("chatrooms", chatroomService.getChatrooms());
        return "chatroom-list";
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
    @GetMapping("/room/{chatroomId}")
    @PostAuthorize("hasPermission(#chatroomId, null)")
    public String chatroom(@PathVariable String chatroomId, Model model) {
        // TODO: put in method
        //        DefaultOAuth2User ud
        //                = (DefaultOAuth2User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        //        String username = ud.getAttributes().getOrDefault("login", "").toString();
        //        log.info("user attribute: {}", username);

        model.addAttribute("chatroomId", chatroomId);
        model.addAttribute("WEBSOCKET_URL", WEBSOCKET_URL);
        model.addAttribute("SUBSCRIBE_DESTINATION", SUBSCRIBE_DESTINATION + "/" + chatroomId);
        model.addAttribute("PUBLISH_DESTINATION", PUBLISH_DESTINATION + "/" + chatroomId);
        return "chatroom";
    }

//    /**
//     * This is called when a chatroom user navigates to a chatroom page and establishes a websocket connection for the
//     * first time.
//     *
//     * @param chatroomId
//     *         the id of the chatroom that user left
//     */
//    @GetMapping("/room/{chatroomId}/join")
//    @ResponseBody
//    public void joinChatroom(@PathVariable String chatroomId) throws JsonProcessingException {
//        // TODO: put in method
//        DefaultOAuth2User ud
//                = (DefaultOAuth2User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        String username = ud.getAttributes().getOrDefault("login", "").toString();
//        log.info("user {} joined chatroom {}", username, chatroomId);
//
//        messageService.sendChatroomMessage(ChatroomEvent.USER_JOINED, chatroomId, username);
//    }
//
//
//    /**
//     * This is called when a chatroom user navigates away from the chatroom page.
//     *
//     * @param chatroomId
//     *         the id of the chatroom that user left
//     */
//    @GetMapping("/room/{chatroomId}/exit")
//    @ResponseBody
//    public void exitChatroom(@PathVariable String chatroomId) throws JsonProcessingException {
//        // TODO: put in method
//        DefaultOAuth2User ud
//                = (DefaultOAuth2User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        String username = ud.getAttributes().getOrDefault("login", "").toString();
//        log.info("user {} exited chatroom {}", username, chatroomId);
//
//        messageService.sendChatroomMessage(ChatroomEvent.USER_LEFT, chatroomId, username);
//    }
}
