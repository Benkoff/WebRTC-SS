package io.github.benkoff.webrtcss.controller;

import io.github.benkoff.webrtcss.domain.Room;
import io.github.benkoff.webrtcss.service.RoomService;
import io.github.benkoff.webrtcss.util.Parser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

@Controller
@ControllerAdvice
public class MainController {
    private final RoomService roomService;
    private final Parser parser;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public MainController(RoomService roomService, Parser parser) {
        this.roomService = roomService;
        this.parser = parser;
    }

    @GetMapping({"", "/", "/index", "/home", "/main"})
    public ModelAndView displayMainPage(Long id, String uuid) {
        ModelAndView modelAndView = new ModelAndView("main");
        modelAndView.addObject("id", id);
        modelAndView.addObject("rooms", roomService.getRooms());
        modelAndView.addObject("uuid", uuid);

        return modelAndView;
    }

    @PostMapping(value = "/room", params = "action=create")
    public ModelAndView processRoomSelection(@ModelAttribute("id") String sid,
                                             @ModelAttribute("uuid") String uuid,
                                             BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            // simplified version, no errors processing
            return new ModelAndView("redirect:/");
        }
        parser.parseId(sid).ifPresent(number -> {
            Optional.ofNullable(uuid).ifPresent(name -> {
                Room room = new Room(number);
                if (roomService.addRoom(room)) {
                    room.setHostName(name);
                    logger.debug("User {} creates Room #{}", name, number);
                }
            });
        });

        return new ModelAndView("redirect:/");
    }

    @GetMapping("/room/{sid}/user/{uuid}")
    public ModelAndView displaySelectedRoom(@PathVariable("sid") String sid,
                                            @PathVariable("uuid") String uuid) {
        // redirect to main page if provided data is invalid
        ModelAndView modelAndView = new ModelAndView("redirect:/");

        if (parser.parseId(sid).isPresent()) {
            Room room = roomService.findRoomByStringId(sid).orElse(null);
            if(room != null) {
                // add this user as a visitor if not a host
                if (!room.getHostName().equals(uuid)) {
                    room.setVisitorName(uuid);
                }
                logger.debug("User {} joins Room #{}", uuid, sid);
                // send to the room
                modelAndView = new ModelAndView("chat_room", "id", sid);
                modelAndView.addObject("uuid", uuid);
            }
        }

        return modelAndView;
    }

    @GetMapping("/room/{sid}/user/{uuid}/exit")
    public ModelAndView processRoomExit(@PathVariable("sid") String sid,
                                        @PathVariable("uuid") String uuid) {
        logger.debug("User {} exits Room #{}", uuid, sid);
        Room room = roomService.findRoomByStringId(sid).orElse(null);
        if(room != null && !room.getHostName().equals(uuid)) {
            room.setVisitorName(uuid);
        }

        return displayMainPage(parser.parseId(sid).orElse(null), uuid);
    }

    @GetMapping("/room/random")
    public ModelAndView requestRandomRoomNumber(@ModelAttribute("uuid") String uuid) {
        return displayMainPage(randomValue(), uuid);
    }

    @GetMapping("/offer")
    public ModelAndView displaySampleSdpOffer() {
        return new ModelAndView("sdp_offer");
    }

    @GetMapping("/stream")
    public ModelAndView displaySampleStreaming() {
        return new ModelAndView("streaming");
    }

    private Long randomValue() {
        return ThreadLocalRandom.current().nextLong(0, 100);
    }
}
