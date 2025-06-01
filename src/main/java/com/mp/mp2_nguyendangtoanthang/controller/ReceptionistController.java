package com.mp.mp2_nguyendangtoanthang.controller;

import com.mp.mp2_nguyendangtoanthang.entity.Receptionist;
import com.mp.mp2_nguyendangtoanthang.repository.ReceptionistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/receptionists")
public class ReceptionistController {
    @Autowired
    private ReceptionistRepository receptionistRepository;

    @GetMapping
    public String showReceptionists(Model model) {
        model.addAttribute("receptionists", receptionistRepository.findAll());
        model.addAttribute("receptionist", new Receptionist());
        return "receptionists";
    }

    @PostMapping
    public String createReceptionist(@ModelAttribute Receptionist receptionist) {
        receptionistRepository.save(receptionist);
        return "redirect:/receptionists";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Receptionist receptionist = receptionistRepository.findById(id).orElseThrow();
        model.addAttribute("receptionist", receptionist);
        return "receptionist_edit";
    }

    @PostMapping("/edit/{id}")
    public String updateReceptionist(@PathVariable Long id, @ModelAttribute Receptionist updated) {
        Receptionist receptionist = receptionistRepository.findById(id).orElseThrow();
        receptionist.setName(updated.getName());
        receptionist.setPhoneNo(updated.getPhoneNo());
        receptionistRepository.save(receptionist);
        return "redirect:/receptionists";
    }

    @PostMapping("/delete/{id}")
    public String deleteReceptionist(@PathVariable Long id) {
        receptionistRepository.deleteById(id);
        return "redirect:/receptionists";
    }

    // Keep REST API
    @RestController
    @RequestMapping("/api/receptionists")
    public static class ReceptionistRestController {
        @Autowired
        private ReceptionistRepository receptionistRepository;

        @GetMapping
        public List<Receptionist> getAll() {
            return receptionistRepository.findAll();
        }

        @PostMapping
        public Receptionist create(@RequestBody Receptionist receptionist) {
            return receptionistRepository.save(receptionist);
        }

        @PutMapping("/{id}")
        public Receptionist update(@PathVariable Long id, @RequestBody Receptionist updated) {
            Receptionist receptionist = receptionistRepository.findById(id).orElseThrow();
            receptionist.setName(updated.getName());
            receptionist.setPhoneNo(updated.getPhoneNo());
            return receptionistRepository.save(receptionist);
        }

        @DeleteMapping("/{id}")
        public void delete(@PathVariable Long id) {
            receptionistRepository.deleteById(id);
        }
    }
}