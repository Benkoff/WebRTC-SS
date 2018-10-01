'use strict';
const socket = new WebSocket('ws://' + window.location.host + '/signal');

const videoButtonOff = document.querySelector('#video_off');
const videoButtonOn = document.querySelector('#video_on');
const audioButtonOff = document.querySelector('#audio_off');
const audioButtonOn = document.querySelector('#audio_on');
const exitButton = document.querySelector('#exit');
const localVideo = document.querySelector('#local_video');
const remoteVideo = document.querySelector('#remote_video');
let localStream;

const mediaConstraints = {
    audio: true,
    video: true
};

// run local video when page load
$(function(){
    getMedia(mediaConstraints);
});

// add an event listener to get to know when a connection is open
socket.onopen = function() {
    console.log('WebSocket connection opened. Ready to send messages');
    // send a message to the server
    socket.send('Client '+localStorage.getItem("uuid")+' connected');
};

// add an event listener for a message being received
socket.onmessage = function(message) {
    console.log('Message received from server: ' + message.data);
};

// a listener for the socket being closed event
socket.onclose = function(message) {
    console.log('Socket has been closed');
};

// an event listener to handle errors
socket.onerror = function(message) {
    console.log("Error: " + message);
};

// mute video
videoButtonOff.onclick = () => {
    mediaConstraints.video = false;
    getMedia(mediaConstraints);
    console.log('Video Off');
};
videoButtonOn.onclick = () => {
    mediaConstraints.video = true;
    getMedia(mediaConstraints);
    console.log('Video On');
};

// mute audio
audioButtonOff.onclick = () => {
    mediaConstraints.audio = false;
    getMedia(mediaConstraints);
    console.log('Audio Off');
};
audioButtonOn.onclick = () => {
    mediaConstraints.audio = true;
    getMedia(mediaConstraints);
    console.log('Audio On');
};

// close socket when exit
exitButton.onclick = () => {
    // send a message to the server
    socket.send('Client '+localStorage.getItem("uuid")+' disconnected');
    socket.close();
};

// initialize media stream.
function getMedia(constraints) {
    if (localStream) {
        localStream.getTracks().forEach(track => {
            track.stop();
        });
    }
    navigator.mediaDevices.getUserMedia(constraints)
        .then(getLocalMediaStream).catch(handleLocalMediaStreamError);
}

// Handles success by adding the MediaStream to the video element.
function getLocalMediaStream(mediaStream) {
    localStream = mediaStream;
    localVideo.srcObject = mediaStream;
}

// Handles error by logging a message to the console with the error message.
function handleLocalMediaStreamError(error) {
    console.log('navigator.getUserMedia error: ', error);
}

// use JSON format
function sendToServer(msg) {
    let msgJSON = JSON.stringify(msg);

    socket.send(msgJSON);
}


