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
 * The body of a STOMP message.
 *
 * @typedef MessageBody
 * @property {string} username the username of the sender
 * @property {string} message the message text
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

    // console.log("message:\n", messageBody);
    // const timeStamp = new Date(Date.parse(messageBody.timeStamp)).toISOString();
    // console.log("messageBody.timeStamp:\n", timeStamp);
    const li = document.createElement("li");
    li.appendChild(document.createTextNode(message.body));
    document.getElementById("messages").appendChild(li);
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

/**
 * The endpoint to call when exiting a chatroom.
 *
 * @type {string}
 */
const EXIT_CHATROOM_URL = `/chat/room/${chatroomId}/exit`;

// notify the chat server when a user navigates away from the chatroom page
window.onbeforeunload = () => {
    fetch(EXIT_CHATROOM_URL).then(null);
    return null;
}
