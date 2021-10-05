import {createClient, sendMessage} from './lib/stomp-client.js';
import autoscroll from "./lib/autoscroll.js";

/**
 * A websocket client that connects by default to the topic corresponding to this webpage's chatroom.
 *
 * @type {StompJs.Client}
 */
const client = createClient(onMessage);

// autoscroll message display
autoscroll(document.getElementById("messages-container"));

/**
 * The possible types of the body of a STOMP message. See ChatMessageType in the "messages" package of the chat server.
 * @readonly
 * @enum
 */
const ChatMessageType = {
    USER_MESSAGE: "USER_MESSAGE",
    CHATROOM_MESSAGE: "CHATROOM_MESSAGE",
    USER_LIST_UPDATE: "USER_LIST_UPDATE",
}

/**
 * The body of a STOMP message. See ChatMessage in the "messages" package of the chat server.
 *
 * @typedef MessageBody
 * @property {ChatMessageType} type the type of message
 * @property {string} username the username of the sender
 * @property {string | Array<string>} message the message text, or a list of usernames depending on message type
 * @property {string} timeStamp the date and time when the message was sent
 * */

/**
 * Handles a websocket message event.
 *
 * @param message {StompJs.Message} the websocket message received
 */
function onMessage(message) {
    /**
     * @type {MessageBody}
     */
    const messageBody = JSON.parse(message.body);

    /**
     * @type {ChatMessageType}
     */
    const messageType = messageBody.type

    switch (messageType) {
        case ChatMessageType.USER_MESSAGE:
        case ChatMessageType.CHATROOM_MESSAGE:
            appendChatMessage(message.body); // refactor appendChatMessage
            break;
        case ChatMessageType.USER_LIST_UPDATE:
            updateUserList(messageBody.message);
            break;
        default:
            throw new Error("Invalid message type")
    }
}

/**
 * Appends a chat message to the chat display.
 *
 * @param messageText {string} the text of the message
 */
function appendChatMessage(messageText) {
    // console.log("message:\n", messageBody);
    // const timeStamp = new Date(Date.parse(messageBody.timeStamp)).toISOString();
    // console.log("messageBody.timeStamp:\n", timeStamp);
    const li = document.createElement("li");
    li.appendChild(document.createTextNode(messageText));
    document.getElementById("messages").appendChild(li);
}

/**
 * Updates the chatroom's user list.
 *
 * @param userListJson {string} a JSON string containing the updated user list for the chatroom
 */
function updateUserList(userListJson) {
    const userList = JSON.parse(userListJson);
    if (!(userList instanceof Array)) {
        throw new Error("Invalid user list");
    }
    const e = document.getElementById("user-list__users")
    e.innerHTML = ""; // clear the list before adding the updated users
    userList.forEach(u => {
        const li = document.createElement("li");
        li.appendChild(document.createTextNode(u));
        e.appendChild(li);
    })
}

// when Enter is pressed in the chat input, send a websocket message with the text content
// and clear the input field
const chatInput = document.getElementById("chat-input");
chatInput.addEventListener("keyup", (e) => {
    if (e.key !== "Enter")
        return;
    const text = chatInput.value;
    if (!text)
        return;
    chatInput.value = "";
    sendMessage(client, text);
});

window.addEventListener("unload", function (event) {
    client.onS(SUBSCRIBE_DESTINATION);
});
