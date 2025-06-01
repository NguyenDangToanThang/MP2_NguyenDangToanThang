package com.mp.mp2_nguyendangtoanthang.controller;

import com.mp.mp2_nguyendangtoanthang.entity.Booking;
import com.mp.mp2_nguyendangtoanthang.entity.Room;
import com.mp.mp2_nguyendangtoanthang.repository.BookingRepository;
import com.mp.mp2_nguyendangtoanthang.repository.GuestRepository;
import com.mp.mp2_nguyendangtoanthang.repository.ReceptionistRepository;
import com.mp.mp2_nguyendangtoanthang.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.temporal.ChronoUnit;
import java.util.List;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private GuestRepository guestRepository;
    @Autowired
    private ReceptionistRepository receptionistRepository;

    @PostMapping
    public Booking book(@RequestBody Booking booking) {
        Room room = roomRepository.findById(booking.getRoom().getRoomNo()).orElseThrow();
        if (!room.getRoomStatus().equals("EMPTY")) throw new IllegalStateException("Room is not available");
        long days = ChronoUnit.DAYS.between(booking.getCheckInDate(), booking.getCheckOutDate());
        booking.setTotalPrice(days * room.getPricePerNight());
        booking.setStatus("BOOKED");
        room.setRoomStatus("OCCUPIED");
        roomRepository.save(room);
        return bookingRepository.save(booking);
    }

    @PutMapping("/checkin/{id}")
    public Booking checkIn(@PathVariable Long id) {
        Booking b = bookingRepository.findById(id).orElseThrow();
        b.setStatus("CHECKED_IN");
        return bookingRepository.save(b);
    }

    @PutMapping("/checkout/{id}")
    public Booking checkOut(@PathVariable Long id) {
        Booking b = bookingRepository.findById(id).orElseThrow();
        b.setStatus("CHECKED_OUT");
        Room room = b.getRoom();
        room.setRoomStatus("EMPTY");
        roomRepository.save(room);
        return bookingRepository.save(b);
    }

    @GetMapping public List<Booking> getAll() { return bookingRepository.findAll(); }
}
