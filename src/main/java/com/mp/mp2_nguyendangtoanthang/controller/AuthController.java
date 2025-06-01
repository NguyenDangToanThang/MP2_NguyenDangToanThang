package com.mp.mp2_nguyendangtoanthang.controller;

import com.mp.mp2_nguyendangtoanthang.entity.Receptionist;
import com.mp.mp2_nguyendangtoanthang.repository.ReceptionistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;

@Controller
public class AuthController {

    @Autowired
    private ReceptionistRepository receptionistRepository;

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        HttpSession session,
                        Model model) {

        Receptionist receptionist = receptionistRepository.findByNameAndPassword(username, password);

        if (receptionist != null) {
            session.setAttribute("loggedInReceptionist", receptionist);
            return "redirect:/dashboard";
        } else {
            model.addAttribute("error", "Tên đăng nhập hoặc mật khẩu không đúng!");
            return "login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        Receptionist receptionist = (Receptionist) session.getAttribute("loggedInReceptionist");
        if (receptionist == null) {
            return "redirect:/login";
        }
        model.addAttribute("receptionist", receptionist);
        return "dashboard";
    }
}
