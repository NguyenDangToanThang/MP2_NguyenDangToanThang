package com.mp.mp2_nguyendangtoanthang.controller;

import com.mp.mp2_nguyendangtoanthang.entity.Guest;
import com.mp.mp2_nguyendangtoanthang.entity.Receptionist;
import com.mp.mp2_nguyendangtoanthang.repository.GuestRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/guests")
public class GuestController {
    @Autowired
    private GuestRepository guestRepository;

    @GetMapping
    public String showGuests(HttpSession session, Model model) {
        Receptionist receptionist = (Receptionist) session.getAttribute("loggedInReceptionist");
        if (receptionist == null) {
            return "redirect:/login";
        }
        model.addAttribute("receptionist", receptionist);
        model.addAttribute("guests", guestRepository.findAll());
        model.addAttribute("guest", new Guest());
        return "guests";
    }

    @PostMapping
    public String createGuest(@ModelAttribute Guest guest) {
        guestRepository.save(guest);
        return "redirect:/guests";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id,HttpSession session, Model model) {
        Guest guest = guestRepository.findById(id).orElseThrow();
        model.addAttribute("guest", guest);
        model.addAttribute("guests", guestRepository.findAll());
        Receptionist receptionist = (Receptionist) session.getAttribute("loggedInReceptionist");
        if (receptionist == null) {
            return "redirect:/login";
        }
        model.addAttribute("receptionist", receptionist);
        return "guest_edit";
    }

    @PostMapping("/edit/{id}")
    public String updateGuest(@PathVariable Long id, @ModelAttribute Guest updated) {
        Guest guest = guestRepository.findById(id).orElseThrow();
        guest.setName(updated.getName());
        guest.setAddress(updated.getAddress());
        guest.setPhoneNo(updated.getPhoneNo());
        guestRepository.save(guest);
        return "redirect:/guests";
    }

    @PostMapping("/delete/{id}")
    public String deleteGuest(@PathVariable Long id) {
        guestRepository.deleteById(id);
        return "redirect:/guests";
    }

    // Keep REST API
    @RestController
    @RequestMapping("/api/guests")
    public static class GuestRestController {
        @Autowired
        private GuestRepository guestRepository;

        @GetMapping
        public List<Guest> getAll() { return guestRepository.findAll(); }
        @PostMapping
        public Guest create(@RequestBody Guest guest) { return guestRepository.save(guest); }
        @PutMapping("/{id}")
        public Guest update(@PathVariable Long id, @RequestBody Guest updated) {
            Guest guest = guestRepository.findById(id).orElseThrow();
            guest.setName(updated.getName());
            guest.setAddress(updated.getAddress());
            guest.setPhoneNo(updated.getPhoneNo());
            return guestRepository.save(guest);
        }
        @DeleteMapping("/{id}")
        public void delete(@PathVariable Long id) {
            guestRepository.deleteById(id);
        }
    }
}