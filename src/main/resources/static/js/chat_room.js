
const socket = new WebSocket('ws://' + window.location.host + '/signal');

// add an event listener, when a connection is open
socket.onopen = function() {
    console.log('WebSocket connection opened. Ready to send messages.');

    // Send a message to the server
    socket.send('Hello from WebSocket client!');
};

// add an event listener, when a message is received
socket.onmessage = function(message) {
    console.log('Message received from server: ' + message);
};

function sendToServer(msg) {
    let msgJSON = JSON.stringify(msg);

    connection.send(msgJSON);
}
