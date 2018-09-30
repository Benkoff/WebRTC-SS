package io.github.benkoff.webrtcss.controller;

import io.github.benkoff.webrtcss.domain.Room;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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
public class MainController {
    // domain service layer substitution since this is a very simple realization w/o services & repos
    private final Set<Room> rooms = new TreeSet<>(Comparator.comparing(Room::getId));
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @GetMapping({"", "/", "/index", "/home", "/main"})
    public ModelAndView displayMainPage(Long id) {
        ModelAndView modelAndView = new ModelAndView("main");
        modelAndView.addObject("id", id);
        modelAndView.addObject("rooms", rooms);

        return modelAndView;
    }

    @PostMapping("/room")
    public ModelAndView processRoomSelection(@ModelAttribute("id") String sid, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            // simplified release, no errors processing
            return new ModelAndView("redirect:/");
        }
        parseId(sid).ifPresent(number -> rooms.add(new Room(number)));
        logger.debug("Create Room#{}", parseId(sid).map(Object::toString).orElse("Error parsing Id!"));

        return new ModelAndView("redirect:/");
    }

    @GetMapping("/room/{sid}")
    public ModelAndView displaySelectedRoom(@PathVariable("sid") String sid) {
        logger.debug("Select Room#{}", parseId(sid).map(Object::toString).orElse("Error parsing Id!"));
        ModelAndView modelAndView = new ModelAndView("redirect:/");
        parseId(sid).ifPresent(id -> {
            rooms.stream().filter(r -> r.getId().equals(id)).findFirst().ifPresent(room -> {
                //TODO create Room model
            });
        });

        return modelAndView;
    }

    @GetMapping("/room/random")
    public ModelAndView requestRandomRoomNumber() {
        return displayMainPage(randomValue());
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
