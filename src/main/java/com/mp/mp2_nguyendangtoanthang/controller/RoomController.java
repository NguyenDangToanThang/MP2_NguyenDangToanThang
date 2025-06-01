package com.mp.mp2_nguyendangtoanthang.controller;

import com.mp.mp2_nguyendangtoanthang.entity.Receptionist;
import com.mp.mp2_nguyendangtoanthang.entity.Room;
import com.mp.mp2_nguyendangtoanthang.repository.RoomRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/rooms")
public class RoomController {
    @Autowired
    private RoomRepository roomRepository;

    @GetMapping
    public String showRooms(HttpSession session, Model model) {
        model.addAttribute("rooms", roomRepository.findAll());
        model.addAttribute("room", new Room());
        model.addAttribute("receptionist", new Receptionist());
        Receptionist receptionist = (Receptionist) session.getAttribute("loggedInReceptionist");
        if (receptionist == null) {
            return "redirect:/login";
        }
        model.addAttribute("receptionist", receptionist);
        return "rooms";
    }

    @PostMapping("/create")
    public String createRoom(@ModelAttribute Room room) {
        room.setRoomStatus("EMPTY");
        roomRepository.save(room);
        return "redirect:/rooms";
    }

    @GetMapping("/edit/{roomNo}")
    public String showEditForm(@PathVariable Integer roomNo, HttpSession session, Model model) {
        Room room = roomRepository.findById(roomNo).orElseThrow();
        model.addAttribute("room", room);
        model.addAttribute("receptionist", new Receptionist());
        Receptionist receptionist = (Receptionist) session.getAttribute("loggedInReceptionist");
        if (receptionist == null) {
            return "redirect:/login";
        }
        model.addAttribute("receptionist", receptionist);
        return "room_edit";
    }

    @PostMapping("/edit/{roomNo}")
    public String updateRoom(@PathVariable Integer roomNo, @ModelAttribute Room updated) {
        Room room = roomRepository.findById(roomNo).orElseThrow();
        room.setLocation(updated.getLocation());
        room.setPricePerNight(updated.getPricePerNight());
        room.setRoomStatus(updated.getRoomStatus());
        roomRepository.save(room);
        return "redirect:/rooms";
    }

    // Keep REST API
    @RestController
    @RequestMapping("/api/rooms")
    public static class RoomRestController {
        @Autowired
        private RoomRepository roomRepository;

        @GetMapping
        public List<Room> getAllRooms() { return roomRepository.findAll(); }

        @GetMapping("/available")
        public List<Room> getAvailableRooms() {
            return roomRepository.findAll().stream()
                    .filter(r -> r.getRoomStatus().equalsIgnoreCase("EMPTY")).toList();
        }

        @PostMapping
        public Room createRoom(@RequestBody Room room) { return roomRepository.save(room); }

        @PutMapping("/{roomNo}")
        public Room updateRoom(@PathVariable Integer roomNo, @RequestBody Room updated) {
            Room room = roomRepository.findById(roomNo).orElseThrow();
            room.setRoomStatus(updated.getRoomStatus());
            return roomRepository.save(room);
        }
    }
}