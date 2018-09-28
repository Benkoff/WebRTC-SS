//based on https://github.com/webrtc/samples/blob/gh-pages/src/content/peerconnection/create-offer/js/main.js

'use strict';

const audioInput = document.querySelector('input#audio');
const restartInput = document.querySelector('input#restart');
const vadInput = document.querySelector('input#vad');
const videoInput = document.querySelector('input#video');

const outputTextarea = document.querySelector('textarea#output');
const createOfferButton = document.querySelector('button#createOffer');

createOfferButton.addEventListener('click', createOffer);

async function createOffer() {
    outputTextarea.value = '';
    const peerConnection = new RTCPeerConnection(null);
    const acx = new AudioContext();
    const dst = acx.createMediaStreamDestination();

    const offerOptions = {
        // New spec states offerToReceiveAudio/Video are of type long (due to
        // having to tell how many "m" lines to generate).
        // http://w3c.github.io/webrtc-pc/#idl-def-RTCOfferAnswerOptions.
        offerToReceiveAudio: (audioInput.checked) ? 1 : 0,
        offerToReceiveVideo: (videoInput.checked) ? 1 : 0,
        voiceActivityDetection: vadInput.checked,
        iceRestart: restartInput.checked
    };

    try {
        const offer = await peerConnection.createOffer(offerOptions);
        // peerConnection.setLocalDescription(offer);
        outputTextarea.value = offer.sdp;
    } catch (e) {
        outputTextarea.value = `Failed to create offer: ${e}`;
    }
}
