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
 * Handles a websocket message event.
 *
 * @param message {StompJs.Message} the websocket message received
 */
function onMessage(message) {
    const li = document.createElement("li");
    li.appendChild(document.createTextNode(message.body));
    document.getElementById("messages").appendChild(li);
}

const testMessageBody = `test message from chatroom ${chatroomId}`;
document.getElementById("test-send")
    .addEventListener("click", () => sendMessage(client, testMessageBody));
