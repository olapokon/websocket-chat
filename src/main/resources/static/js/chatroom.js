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
 * Runs once when the page is first loaded.
 *
 * Sends requests to the server until an initial list of users has been fetched.
 *
 * This is to ensure that the list of users does not remain empty when first connected to a chatroom.
 */
(async function onWebSocketConnectionActive() {
    // TODO: add "connecting..." message while not connected
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
 * Enumeration of custom STOMP headers used by the chat server.
 * <p>
 * In sync with CustomStompHeader in the "messages" package of the chat server.
 * @readonly
 * @enum
 * @see CustomStompHeader in the chat server
 */
const CustomStompHeader = {
    MESSAGE_TYPE: "message-type",
    TIMESTAMP: "chat-timestamp",
}

/**
 * Enumeration of types of value of a {CustomStompHeader}.
 * <p>
 * In sync with ChatMessageType in the "messages" package of the chat server.
 * @readonly
 * @enum
 * @see ChatMessageType in the chat server
 */
const ChatMessageType = {
    USER_MESSAGE: "user-message",
    USER_JOINED: "user-joined",
    USER_LEFT: "user-left",
    USER_LIST_UPDATE: "user-list-update",
    USER_LIST_UPDATE_REQUEST: "user-list-update-request",
}

/**
 * Parses a {CustomStompHeader#MESSAGE_TYPE} header.
 *
 * @param headerValue {string} the value of the header
 * @return {[string, string]} the type and value of the header value
 */
function parseMessageTypeHeaderValue(headerValue) {
    const HEADER_VALUE_SEPARATOR = "; ";
    const h = headerValue.split(HEADER_VALUE_SEPARATOR);
    if (h.length > 2) {
        throw new Error("Invalid " + CustomStompHeader.MESSAGE_TYPE + " header")
    }

    /**
     * @enum {ChatMessageType}
     */
    const type = h[0];
    if (!Object.values(ChatMessageType).includes(type))
        throw new Error("Invalid " + CustomStompHeader.MESSAGE_TYPE + " header")
    const value = h[1];
    return [type, value];
}

/**
 * Keeps track of the last ACKed STOMP message.
 */
let lastAckedMessage;

/**
 * Handles a websocket message event.
 *
 * @param message {StompJs.Message} the websocket message received
 */
function onMessage(message) {
    const headers = message.headers;
    const messageBody = message.body;

    const messageTypeHeader = headers[CustomStompHeader.MESSAGE_TYPE];
    if (!messageTypeHeader) {
        return;
    }

    const timestamp = headers[CustomStompHeader.TIMESTAMP]; // TODO: parse
    if (!timestamp) {
        throw new Error("Invalid " + CustomStompHeader.MESSAGE_TYPE + " header")
    }

    const [messageType, messageTypeValue] = parseMessageTypeHeaderValue(messageTypeHeader);
    let username = "";
    switch (messageType) {
        case ChatMessageType.USER_MESSAGE:
            username = messageTypeValue;
            appendChatMessage(`[${timestamp}] ${username}: ${messageBody}`);
            break;
        case ChatMessageType.USER_JOINED:
            username = messageTypeValue;
            appendChatMessage(`${username} has joined the chat.`);
            break;
        case ChatMessageType.USER_LEFT:
            username = messageTypeValue;
            appendChatMessage(`${username} has left the chat.`);
            break;
        case ChatMessageType.USER_LIST_UPDATE:
            updateUserList(messageTypeValue);
            break;
        case ChatMessageType.USER_LIST_UPDATE_REQUEST:
        default:
            throw new Error("Invalid " + CustomStompHeader.MESSAGE_TYPE + " header")
    }

    // TODO message ACK or NACK
    message.ack();
    lastAckedMessage = message;
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
