package io.github.benkoff.webrtcss.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

@Controller
public class MainController {

    @GetMapping({"", "/", "/index", "/home", "/main"})
    public ModelAndView displayMainPage(Long id) {
        ModelAndView modelAndView = new ModelAndView("main");
        modelAndView.addObject("id", id);

        return modelAndView;
    }

    @PostMapping("/room")
    public ModelAndView processRoomSelection(@ModelAttribute("id") String sid, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            //TODO errors processing
            return new ModelAndView("redirect:/");
        }
        //TODO process room
        Long id = null;
        try {
            id = Long.valueOf(sid);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(Optional.ofNullable(id).map(Object::toString).orElse("Error parsing Id!"));

        return new ModelAndView("redirect:/");
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
}
