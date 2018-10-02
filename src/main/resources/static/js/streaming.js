//based on client from https://codelabs.developers.google.com/codelabs/webrtc-web/#3
'use strict';

const vgaButton = document.querySelector('#vga');
const qvgaButton = document.querySelector('#qvga');
const hdButton = document.querySelector('#hd');
// Video element where stream will be placed.
const localVideo = document.querySelector('video');
// Local stream that will be reproduced on the video.
let localStream;
// media constraints
const qvgaConstraints = {
    video: {width: {exact: 320}, height: {exact: 240}},
    audio: true
};
const vgaConstraints = {
    video: {width: {exact: 640}, height: {exact: 480}},
    audio: true
};
const hdConstraints = {
    video: {width: {exact: 1280}, height: {exact: 720}},
    audio: true
};

// Get Media with selected constraints
qvgaButton.onclick = () => {
    getMedia(qvgaConstraints);
};

vgaButton.onclick = () => {
    getMedia(vgaConstraints);
};

hdButton.onclick = () => {
    getMedia(hdConstraints);
};

// Initializes media stream.
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
