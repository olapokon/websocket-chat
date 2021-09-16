// TODO: https://stomp-js.github.io/guide/stompjs/using-stompjs-v5.html

const SERVER_PORT = 8080;
const WEBSOCKET_ENDPOINT = "ws"
const WEBSOCKET_URL = `ws://localhost:${SERVER_PORT}/${WEBSOCKET_ENDPOINT}`;

const SUBSCRIBE_DESTINATION = "/topic/all";

const APPLICATION_DESTINATION_PREFIX = "/app";
const APPLICATION_DESTINATION = "/ws-test";
const CUSTOM_DESTINATION = "/custom-endpoint"
const PUBLISH_DESTINATION = `${APPLICATION_DESTINATION_PREFIX}${APPLICATION_DESTINATION}${CUSTOM_DESTINATION}`;

let messageCount = 0;

let client;
client = new StompJs.Client({
    brokerURL: WEBSOCKET_URL,
    connectHeaders: {
        // login: 'user',
        // passcode: 'password',
    },
    debug: function (str) { // DEBUG console output
        console.debug(str);
    },
    reconnectDelay: 5000,
    heartbeatIncoming: 4000,
    heartbeatOutgoing: 4000,
});

client.onConnect = async function (frame) {
    console.log("connected:\n", frame);
    // Do something, all subscribes must be done is this callback
    // This is needed because this will be executed after a (re)connect

    function cb(message) {
        // called when the client receives a STOMP message from the server
        // if (message.body)
        //     console.log('got message with body:\n', message.body);
        // else
        //     console.log('got empty message');
    }

    const headers = {ack: 'client'};
    await client.subscribe(SUBSCRIBE_DESTINATION, cb, headers);

    sendMessage();
};

client.onStompError = function (frame) {
    // Will be invoked in case of error encountered at Broker
    // Bad login/passcode typically will cause an error
    // Complaint brokers will set `message` header with a brief message. Body may contain details.
    // Compliant brokers will terminate the connection after any error
    console.log('Broker reported error: ' + frame.headers['message']);
    console.log('Additional details: ' + frame.body);
};

client.activate();

let intervalID;

function sendMessage() {
    if (intervalID)
        clearInterval(intervalID);

    intervalID = setInterval(() => {
        const messageBody = `test message #${++messageCount}`;
        // console.log("sent message with body:\n", messageBody)
        client.publish({
            destination: PUBLISH_DESTINATION,
            body: messageBody,
            skipContentLengthHeader: true,
        });
    }, 1000);
}
