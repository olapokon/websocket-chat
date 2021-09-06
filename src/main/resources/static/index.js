// TODO: https://stomp-js.github.io/guide/stompjs/using-stompjs-v5.html

const WEBSOCKET_URL = 'ws://localhost:8080/ws';

const client = new StompJs.Client({
	brokerURL: WEBSOCKET_URL,
	connectHeaders: {
		// login: 'user',
		// passcode: 'password',
	},
	debug: function (str) { // DEBUG console output
		console.log(str);
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
		if (message.body) {
			console.log('got message with body:\n', message.body);
		} else {
			console.log('got empty message');
		}
	}

	const headers = {ack: 'client'};
	await client.subscribe('/queue', cb, headers);

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
