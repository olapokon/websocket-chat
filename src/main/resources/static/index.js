const client = new StompJs.Client({
    brokerURL: 'ws://localhost:8080/ws',
    connectHeaders: {
        // login: 'user',
        // passcode: 'password',
    },
    debug: function (str) {
        console.log(str);
    },
    reconnectDelay: 5000,
    heartbeatIncoming: 4000,
    heartbeatOutgoing: 4000,
});

client.onConnect = function (frame) {
    console.log("connected:\n", frame);
    // Do something, all subscribes must be done is this callback
    // This is needed because this will be executed after a (re)connect

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

function sendMessage() {
    setInterval(() => {
        client.publish({
            destination: '/app/ws-test',
            body: 'A A A A A',
            skipContentLengthHeader: true,
        });
    }, 1000);
}
