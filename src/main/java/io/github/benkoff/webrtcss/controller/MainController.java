package io.github.benkoff.webrtcss.controller;

import io.github.benkoff.webrtcss.service.MainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@ControllerAdvice
public class MainController {    
    private final MainService mainService;
    
    @Autowired
    public MainController(final MainService mainService) {
        this.mainService = mainService;
    }

    @GetMapping({"", "/", "/index", "/home", "/main"})
    public ModelAndView displayMainPage(final Long id, final String uuid) {
        return this.mainService.displayMainPage(id, uuid);
    }

    @PostMapping(value = "/room", params = "action=create")
    public ModelAndView processRoomSelection(@ModelAttribute("id") final String sid, @ModelAttribute("uuid") final String uuid, final BindingResult binding) {
        return this.mainService.processRoomSelection(sid, uuid, binding);
    }

    @GetMapping("/room/{sid}/user/{uuid}")
    public ModelAndView displaySelectedRoom(@PathVariable("sid") final String sid, @PathVariable("uuid") final String uuid) {
        return this.mainService.displaySelectedRoom(sid, uuid);
    }

    @GetMapping("/room/{sid}/user/{uuid}/exit")
    public ModelAndView processRoomExit(@PathVariable("sid") final String sid, @PathVariable("uuid") final String uuid) {
        return this.mainService.processRoomExit(sid, uuid);
    }

    @GetMapping("/room/random")
    public ModelAndView requestRandomRoomNumber(@ModelAttribute("uuid") final String uuid) {
        return mainService.requestRandomRoomNumber(uuid);
    }

    @GetMapping("/offer")
    public ModelAndView displaySampleSdpOffer() {
        return new ModelAndView("sdp_offer");
    }

    @GetMapping("/stream")
    public ModelAndView displaySampleStreaming() {
        return new ModelAndView("streaming");
    }
}
