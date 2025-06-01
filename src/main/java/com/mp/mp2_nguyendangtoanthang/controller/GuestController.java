package com.mp.mp2_nguyendangtoanthang.controller;

import com.mp.mp2_nguyendangtoanthang.entity.Guest;
import com.mp.mp2_nguyendangtoanthang.repository.GuestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/guests")
public class GuestController {
    @Autowired
    private GuestRepository guestRepository;

    @GetMapping
    public List<Guest> getAll() { return guestRepository.findAll(); }
    @PostMapping
    public Guest create(@RequestBody Guest guest) { return guestRepository.save(guest); }
    @PutMapping("/{id}") public Guest update(@PathVariable Long id, @RequestBody Guest updated) {
        Guest guest = guestRepository.findById(id).orElseThrow();
        guest.setName(updated.getName()); guest.setAddress(updated.getAddress()); guest.setPhoneNo(updated.getPhoneNo());
        return guestRepository.save(guest);
    }
    @DeleteMapping("/{id}") public void delete(@PathVariable Long id) { guestRepository.deleteById(id); }
}