package com.example.demo.kintai;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
@Controller
public class HomeController {

    @GetMapping("/home")
    public String home(Model model) {
        model.addAttribute("content", "home");
        return "layout";
    }

    @GetMapping("/settings")
    public String settings(Model model) {
        model.addAttribute("content", "settings");
        return "layout";
    }
}
