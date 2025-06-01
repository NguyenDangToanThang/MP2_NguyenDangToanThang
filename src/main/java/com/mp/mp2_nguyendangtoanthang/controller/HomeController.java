package com.mp.mp2_nguyendangtoanthang.controller;

import com.mp.mp2_nguyendangtoanthang.entity.Receptionist;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home(HttpSession session, Model model) {
        Receptionist receptionist = (Receptionist) session.getAttribute("loggedInReceptionist");
        if (receptionist == null) {
            return "redirect:/login";
        }
        model.addAttribute("receptionist", receptionist);
        return "dashboard";
    }
}