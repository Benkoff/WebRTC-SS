# WebRTC-SS
## WebRTC Signaling Server & Simple Video Chat

## Simple Spring Boot signaling server to provide basic functionality:
1. Create chat rooms prividing their links publishing;
2. Establish client-service WebSocket connections; 
3. Implement WebRTS SDP Offers and ICE Candidates negotiation;
4. Test peer-to-peer video chatting;

### TODO
1. Test streaming audio & video with getUserMedia(), constraints -- Client
2. Room Creation, joining -- Client & Server
3. Messaging with WebSocket -- Client & Server
4. Peer connection, signaling, ICE, SDP Offers -- Client & Server

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
* https://keyholesoftware.com/2017/04/10/websockets-with-spring-boot/
* https://www.baeldung.com/websockets-spring
