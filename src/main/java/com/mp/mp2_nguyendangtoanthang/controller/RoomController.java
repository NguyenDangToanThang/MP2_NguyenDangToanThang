package com.mp.mp2_nguyendangtoanthang.controller;

import com.mp.mp2_nguyendangtoanthang.entity.Room;
import com.mp.mp2_nguyendangtoanthang.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rooms")
public class RoomController {
    @Autowired
    private RoomRepository roomRepository;

    @GetMapping
    public List<Room> getAllRooms() { return roomRepository.findAll(); }

    @GetMapping("/available")
    public List<Room> getAvailableRooms() {
        return roomRepository.findAll().stream().filter(r -> r.getRoomStatus().equalsIgnoreCase("EMPTY")).toList();
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