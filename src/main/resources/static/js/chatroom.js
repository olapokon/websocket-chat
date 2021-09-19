import {createClient} from './lib/stomp-client.js';
import autoscroll from "./lib/autoscroll.js";

const client = createClient(onMessage);

let messageCount = 0;
const testMessageBody = `test message from chatroom ${chatroomId}`;

/**
 * Sends a websocket message.
 *
 * @param client {StompJs.Client} the websocket client to use
 * @param messageBody the body of the message
 */
function sendMessage(client, messageBody) {
    // expect the connection options to be available
    if (!PUBLISH_DESTINATION)
        throw new Error("Missing arguments needed to send websocket message.");

    client.publish({
        destination: PUBLISH_DESTINATION,
        body: messageBody + ` #${++messageCount}`,
        skipContentLengthHeader: true,
    });
}

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

document.getElementById("test-send")
    .addEventListener("click", () => sendMessage(client, testMessageBody));

// autoscroll message display
autoscroll(document.getElementById("messages-container"));
