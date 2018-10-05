'use strict';
const socket = new WebSocket('ws://' + window.location.host + '/signal:6503');
const peerConnectionConfig = {
    'iceServers': [
        {'urls': 'stun:stun.stunprotocol.org:3478'},
        {'urls': 'stun:stun.l.google.com:19302'},
    ]
};

const videoButtonOff = document.querySelector('#video_off');
const videoButtonOn = document.querySelector('#video_on');
const audioButtonOff = document.querySelector('#audio_off');
const audioButtonOn = document.querySelector('#audio_on');
const exitButton = document.querySelector('#exit');
const localVideo = document.querySelector('#local_video');
const remoteVideo = document.querySelector('#remote_video');
const localRoom = document.querySelector('input#id').value;
const localUserName = localStorage.getItem("uuid");

let localStream;
let localVideoTracks;
let myPeerConnection;

const mediaConstraints = {
    audio: true,
    video: true
};

// run when page loads
$(function(){
    start();
});

// add an event listener to get to know when a connection is open
socket.onopen = function() {
    console.log('WebSocket connection opened. Room: ' + localRoom);
    // send a message to the server
    sendToServer({
        from: localUserName,
        type: 'join',
        data: localRoom
    });
};

// add an event listener for a message being received
socket.onmessage = function(message) {
    const webSocketMessage = JSON.parse(message.data);
    if (webSocketMessage.type === 'text') {
        console.log('Text message from ' + webSocketMessage.from + ' received: ' + webSocketMessage.data);
    } else if (webSocketMessage.type === 'signal') {
        console.log('Signal received from server');
        if (message.data.sdp) {
            console.log('SDP received from server');

            myPeerConnection.setRemoteDescription(
                new RTCSessionDescription(webSocketMessage.data.sdp))
                .then(function() {
                    if (myPeerConnection.remoteDescription.type === 'offer') {
                        handleOfferMessage(message);
                        //TODO
                        console.log('remoteDescription.type == offer');
                    }
                });
        } else {
            console.log('Candidate received from server');
            // myPeerConnection.addIceCandidate(new RTCIceCandidate(message.data.candidate));
            //TODO
        }
    } else {
        // this should never happen
        console.log('Error: Wrong type message received from server');
    }
};

// a listener for the socket being closed event
socket.onclose = function(message) {
    console.log('Socket has been closed');
};

// an event listener to handle errors
// socket.onerror = function(message) {
//     console.log("Error: " + message);
// };

// use JSON format to send WebSocket message
function sendToServer(msg) {
    let msgJSON = JSON.stringify(msg);
    socket.send(msgJSON);
}

// mute video
videoButtonOff.onclick = () => {
    localVideoTracks = localStream.getVideoTracks();
    localVideoTracks.forEach(track => localStream.removeTrack(track));
    $(localVideo).css('display', 'none');
    console.log('Video Off');
};
videoButtonOn.onclick = () => {
    localVideoTracks.forEach(track => localStream.addTrack(track));
    $(localVideo).css('display', 'inline');
    console.log('Video On');
};

// mute audio
audioButtonOff.onclick = () => {
    localVideo.muted = true;
    console.log('Audio Off');
};
audioButtonOn.onclick = () => {
    localVideo.muted = false;
    console.log('Audio On');
};

// close socket when exit
exitButton.onclick = () => {
    stop();
};

// create peer connection, init local stream
function start() {
    createPeerConnection();
    getMedia(mediaConstraints);
}

function stop() {
    // send a message to the server
    sendToServer({
        from: localUserName,
        type: 'text',
        data: 'Client ' + localUserName +' disconnected'
    });
    if (socket != null) {
        socket.close();
    }
    console.log('Socket closed');
}

// initialize media stream
function getMedia(constraints) {
    if (localStream) {
        localStream.getTracks().forEach(track => {
            track.stop();
        });
    }
    navigator.mediaDevices.getUserMedia(constraints)
        .then(getLocalMediaStream).catch(handleGetUserMediaError);
}

// handle get media error
function handleGetUserMediaError(error) {
    console.log('navigator.getUserMedia error: ', error);
    switch(error.name) {
        case "NotFoundError":
            alert("Unable to open your call because no camera and/or microphone" +
                "were found.");
            break;
        case "SecurityError":
        case "PermissionDeniedError":
            // Do nothing; this is the same as the user canceling the call.
            break;
        default:
            alert("Error opening your camera and/or microphone: " + error.message);
            break;
    }

    stop();
}

// add MediaStream to local video element and to the Peer
function getLocalMediaStream(mediaStream) {
    localStream = mediaStream;
    localVideo.srcObject = mediaStream;
    localStream.getTracks().forEach(track => myPeerConnection.addTrack(track, localStream));
}

function createPeerConnection() {
    myPeerConnection = new RTCPeerConnection(peerConnectionConfig);

    myPeerConnection.onicecandidate = handleICECandidateEvent;
    // myPeerConnection.ontrack = handleTrackEvent;
    myPeerConnection.onnegotiationneeded = handleNegotiationNeededEvent;
    // myPeerConnection.onremovetrack = handleRemoveTrackEvent;
    // myPeerConnection.oniceconnectionstatechange = handleICEConnectionStateChangeEvent;
    // myPeerConnection.onicegatheringstatechange = handleICEGatheringStateChangeEvent;
    // myPeerConnection.onsignalingstatechange = handleSignalingStateChangeEvent;
}

function handleICECandidateEvent(event) {
    if (event.candidate) {
        sendToServer({
            from: localUserName,
            type: 'signal',
            data: event.candidate
        });
        console.log('handleICECandidateEvent: ICE candidate sent');
    }
}

function handleOfferMessage(message) {
    myPeerConnection.createAnswer().then(function(answer) {
        return myPeerConnection.setLocalDescription(answer);
    })
        .then(function() {
            sendToServer({
                from: localUserName,
                type: 'signal',
                data: {
                    'sdp': myPeerConnection.localDescription
                }
            });
            console.log('handleOfferMessage: SDP answer sent');
        })
        .catch(function(reason) {
            // an error occurred, so handle the failure to connect
            console.log('failure to connect error: ', reason);
        });
}

function handleNegotiationNeededEvent() {
    myPeerConnection.createOffer().then(function(offer) {
        return myPeerConnection.setLocalDescription(offer);
    })
        .then(function() {
            sendToServer({
                from: localUserName,
                type: 'signal',
                data: {
                    'sdp': myPeerConnection.localDescription
                }
            });
            console.log('handleNegotiationNeededEvent: SDP offer sent');
        })
        .catch(function(reason) {
            // an error occurred, so handle the failure to connect
            console.log('failure to connect error: ', reason);
        });
}

//TODO replace this temporary solution
function logError(error) {
    console.log(error.name + ': ' + error.message);
}
