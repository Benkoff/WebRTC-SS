package io.github.benkoff.webrtcss.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class MainController {
    @GetMapping({"", "/", "/index", "/home", "/main"})
    public ModelAndView displayMainPage() {
        return new ModelAndView("main");
    }
}
