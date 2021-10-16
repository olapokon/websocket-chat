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
 * Keeps track of whether the user list has been updated at least once,
 * to make sure it does not remain empty after first loading the page and connecting to the destination.
 *
 * @type {boolean}
 */
let initialUserListAcquired = false;

/**
 * Fetches and returns the a Promise that resolves to a json string
 * containing the list of usernames for the users
 * currently connected to this chatroom/destination, or null if it is unsuccessful.
 *
 * @return {Promise<string>}
 */
function fetchUserList() {
    // TODO refactor to request the user list through a websocket message?
    return fetch(`/chat/${chatroomId}/user-list`)
        .then(userList => userList.text())
        .catch(() => null);
}

/**
 * Sends requests to the server until an initial list of users has been fetched.
 *
 * This is to ensure that the list of users does not remain empty when first connected to a chatroom.
 */
(async function onWebSocketConnectionActive() {
    const TIMEOUT = 200;
    if (initialUserListAcquired) {
        // stop attempting to retrieve a user list if it has already been fetched once
        return;
    }
    if (!client.connected) {
        console.log("CLIENT NOT CONNECTED");
        setTimeout(onWebSocketConnectionActive, TIMEOUT);
        return;
    }
    const userListFetched = await fetchUserList();
    console.log("userListFetched:\n", userListFetched);
    if (!userListFetched) {
        setTimeout(onWebSocketConnectionActive, TIMEOUT);
        return;
    }
    try {
        updateUserList(userListFetched);
    } catch (_) {
        setTimeout(onWebSocketConnectionActive, TIMEOUT);
        return;
    }
})();

/**
 * The possible types of the body of a STOMP frame. See ChatMessageType in the "messages" package of the chat server.
 * @readonly
 * @enum
 */
const ChatMessageType = {
    USER_MESSAGE: "USER_MESSAGE",
    CHATROOM_MESSAGE: "CHATROOM_MESSAGE",
    USER_LIST_UPDATE: "USER_LIST_UPDATE",
}

/**
 * The body of a STOMP frame. See ChatMessage in the "messages" package of the chat server.
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
    console.log("messageHeaders:\n:", message.headers);

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
    console.log("UPDATING USER LIST: " + (userList instanceof Array));
    if (!(userList instanceof Array)) {
        throw new Error("Invalid user list");
    }
    const e = document.getElementById("user-list__users")
    e.innerHTML = ""; // clear the list before adding the updated users
    userList.forEach(u => {
        const li = document.createElement("li");
        li.appendChild(document.createTextNode(u));
        e.appendChild(li);
    });
    initialUserListAcquired = true;
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
