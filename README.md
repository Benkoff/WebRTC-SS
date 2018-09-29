# WebRTC-SS
## WebRTC Signaling Server

## This is a simple signaling server to provide the following functionality:
1. Client's WebSocket connection establishing;
2. New chat rooms creation;
3. Created chat room links publishing;
4. Chat Room clients' SDP Offers and ICE Candidates exchanging;

### TODO
1. Streaming audio & video with getUserMedia(), constraints -- Client
2. SDP generation. Offer & Answer -- Client & Server
3. ICE candidates exchange -- Client & Server
4. Combine peer connection and signaling -- Client & Server

**Documentation and Tutorials:**
* https://developer.mozilla.org/en-US/docs/Web/API/WebRTC_API/Signaling_and_video_calling
* https://codelabs.developers.google.com/codelabs/webrtc-web/
* https://webrtc.github.io/samples/
* https://andrewjprokop.wordpress.com/2014/07/21/understanding-webrtc-media-connections-ice-stun-and-turn/
* https://nextrtc.org/
* https://www.html5rocks.com/en/tutorials/webrtc/basics/
* http://w3c.github.io/webrtc-pc/
* https://www.scaledrone.com/blog/webrtc-chat-tutorial/
* http://builds.kurento.org/release/stable/docs/tutorials/node/tutorial-4-one2one.html

**Sample Implementations:**
* https://github.com/webrtc/samples
* https://github.com/webrtc/apprtc/blob/master/src/collider/collider/collider.go
* https://github.com/mslosarz/nextrtc-signaling-server
* https://github.com/Kurento/kurento-tutorial-java/blob/master/kurento-one2one-call-advanced/src/main/java/org/kurento/tutorial/one2onecalladv

**WebSockets:**
* https://docs.spring.io/spring/docs/current/spring-framework-reference/web.html#websocket
* https://www.baeldung.com/java-websockets
* https://www.baeldung.com/websockets-spring
