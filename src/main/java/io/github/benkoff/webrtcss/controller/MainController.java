package io.github.benkoff.webrtcss.controller;

import io.github.benkoff.webrtcss.domain.Room;
import io.github.benkoff.webrtcss.domain.RoomService;
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

import java.util.Comparator;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ThreadLocalRandom;

@Controller
@ControllerAdvice
public class MainController {
    private final RoomService roomService;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public MainController(RoomService roomService) {
        this.roomService = roomService;
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
            // simplified release, no errors processing
            return new ModelAndView("redirect:/");
        }
        parseId(sid).ifPresent(number -> {
            Optional.ofNullable(uuid).ifPresent(name -> {
                Room room = new Room(number);
                roomService.getRooms().add(room);
                room.setHostName(name);
                logger.debug("Host {} creates Room#{}", name, number);
            });
        });

        return new ModelAndView("redirect:/");
    }

    @GetMapping("/room/{sid}/user/{uuid}")
    public ModelAndView displaySelectedRoom(@PathVariable("sid") String sid,
                                            @PathVariable("uuid") String uuid) {
        // Link to redirect if provided data invalid
        ModelAndView modelAndView = new ModelAndView("redirect:/");

        if (parseId(sid).isPresent()) {
            // find the room
            Room room = roomService.getRooms().stream()
                    .filter(r -> r.getId().equals(parseId(sid).get()))
                    .findFirst()
                    .orElse(null);
            if(room != null) {
                // add this visitor if not a host
                if (!room.getHostName().equals(uuid)) {
                    room.setVisitorName(uuid);
                }
                logger.debug("User {} joins Room#{}", uuid, sid);
                // send to the room
                modelAndView = new ModelAndView("chat_room", "id", sid);
                modelAndView.addObject("uuid", uuid);
            }
        }

        return modelAndView;
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

    private Optional<Long> parseId(String sid) {
        Long id = null;
        try {
            id = Long.valueOf(sid);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return Optional.ofNullable(id);
    }
}
