// https://stomp-js.github.io/guide/stompjs/using-stompjs-v5.html

/**
 * Creates a stomp-js Client, using options passed from java, through thymeleaf inline javascript
 *
 * @param onMessage {() => void} websocket message event callback
 * @returns {StompJs.Client} a stomp-js client
 */
export function createClient(onMessage) {
    // expect the connection options to be available
    if (!WEBSOCKET_URL
        || !SUBSCRIBE_DESTINATION
        || !PUBLISH_DESTINATION)
        throw new Error("Missing arguments needed to establish websocket connection.");

    const client = new StompJs.Client({
        brokerURL: WEBSOCKET_URL,
        connectHeaders: {},
        // debug: (str) => console.debug(str), // DEBUG console output
        reconnectDelay: 1000,
        heartbeatIncoming: 4000,
        heartbeatOutgoing: 4000,
    });

    client.onConnect = async function (frame) {
        console.log("connected:\n", frame);
        // Do something, all subscribes must be done is this callback
        // This is needed because this will be executed after a (re)connect

        const headers = {ack: 'client'};
        client.subscribe(SUBSCRIBE_DESTINATION, onMessage, headers);
    };

    client.onStompError = function (frame) {
        // Will be invoked in case of error encountered at Broker
        // Bad login/passcode typically will cause an error
        // Complaint brokers will set `message` header with a brief message. Body may contain details.
        // Compliant brokers will terminate the connection after any error
        console.log('Broker reported error:\n', frame.headers['message']);
        console.log('Additional details:\n', frame.body);
    };

    client.activate();

    return client;
}

/**
 * Sends a websocket message.
 *
 * @param client {StompJs.Client} the websocket client to use
 * @param messageBody the body of the message
 */
export function sendMessage(client, messageBody) {
    // expect the connection options to be available
    if (!PUBLISH_DESTINATION)
        throw new Error("Missing arguments needed to send websocket message.");

    client.publish({
        destination: PUBLISH_DESTINATION,
        body: messageBody,
        skipContentLengthHeader: true,
    });
}
