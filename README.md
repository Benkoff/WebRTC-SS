# WebRTC-SS
## WebRTC Signaling Server & Simple Video Chat

## Simple Signaling Server to provide basic WebRTC functionality
1. Create chat rooms providing their links web publishing;
2. Establish client-service WebSocket connections; 
3. Implement WebRTC SDP Offers and ICE Candidates negotiation;
4. Test peer-to-peer video chatting.

### Project Desription
This project has been built with Gradle, server side is written in Java 8 using Spring Boot, client is in Java Script.
Application web entry: http://localhost:8080 (https://localhost:4883 for the Secure Test version on the test branch).
The back side uses the following frameworks and technologies:
* Spring Boot 2.0.5 with starter web, validation, and thymeleaf dependencies needed for the web server;
* Spring Web Socket to establish connection between signalling server and clients;
* HTML 5 to provide Web RTC interaction between clients;
* Spring Boot Starter-test provides JUnit 4, Mockito, AssertJ, other libraries used to test back-side.

### Git Repository Structure
This GitHub repository has 3 branches:
* main - latest stable version working on http://localhost:8080
* develop - to maintain product development;
* test - includes self-signed sertificates to test video chatting on mobile devices (such as Android Chromium, which forbid http connection to local host), use https://localhost:4883 to reach this version, and let your browser ingnore a warning about the certificate.

### Project Structure
* MainController provides HTTP requests handling, model processing and view presentation;
* Domain package includes domain model and service;
* Web Socket based Web RTC Signalling Server is located under the Socket directory;
* Config and Util packages contain configuration and utility classes respectively;
* Test section contains unit and integration backside tests;
* Resources folder keeps application.yml configuration file, certificates (in test branch), frontside templates and static content (CSS, Java Script files).

### API
Method |      URI           |  Description
 ------ | --------------------------------------------------- | ------- 
 Get | "", "/", "/index", "/home", "/main" | main page application web entry point
 Post | "/room" | process room selection form
 Get | "/room/{sid}/user/{uuid}" | select a room to enter; sid - room number, uuid - user id
 Get | "/room/{sid}/user/{uuid}/exit" | a room exit point for the user selected
 Get | "/room/random" | generates random room number
 Get | "/offer" | demonstrates sample SDP offer
 Get | "/stream" | demonstrates streaming video resolution selection
 
### Building and Running Docker Image
1. Build a Docker image running in the Terminal ``./gradlew build docker --info``
2. Select recently built image, tagged ``benkoff/webrtcss-spring-boot-docker:latest``
3. Push selected image to Docker hub
4. Pull and run uploaded image in local Docker with ``docker run -p 8080:8080 -t benkoff/webrtcss-spring-boot-docker
``

### Useful Links and Sources
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
