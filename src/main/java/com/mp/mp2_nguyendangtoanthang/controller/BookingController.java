package com.mp.mp2_nguyendangtoanthang.controller;

import com.mp.mp2_nguyendangtoanthang.entity.Booking;
import com.mp.mp2_nguyendangtoanthang.entity.Room;
import com.mp.mp2_nguyendangtoanthang.repository.BookingRepository;
import com.mp.mp2_nguyendangtoanthang.repository.GuestRepository;
import com.mp.mp2_nguyendangtoanthang.repository.ReceptionistRepository;
import com.mp.mp2_nguyendangtoanthang.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.temporal.ChronoUnit;
import java.util.List;

@Controller
@RequestMapping("/bookings")
public class BookingController {
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private GuestRepository guestRepository;
    @Autowired
    private ReceptionistRepository receptionistRepository;

    @GetMapping
    public String showBookings(Model model) {
        model.addAttribute("bookings", bookingRepository.findAll());
        return "bookings";
    }

    @GetMapping("/create")
    public String showCreateBookingForm(Model model) {
        model.addAttribute("booking", new Booking());
        model.addAttribute("guests", guestRepository.findAll());
        model.addAttribute("rooms", roomRepository.findAll().stream()
                .filter(r -> r.getRoomStatus().equals("EMPTY")).toList());
        model.addAttribute("receptionists", receptionistRepository.findAll());
        return "booking_create";
    }

    @PostMapping("/create")
    public String createBooking(@ModelAttribute Booking booking) {
        Room room = roomRepository.findById(booking.getRoom().getRoomNo()).orElseThrow();
        if (!room.getRoomStatus().equals("EMPTY")) {
            throw new IllegalStateException("Room is not available");
        }
        long days = ChronoUnit.DAYS.between(booking.getCheckInDate(), booking.getCheckOutDate());
        booking.setTotalPrice(days * room.getPricePerNight());
        booking.setStatus("BOOKED");
        room.setRoomStatus("OCCUPIED");
        roomRepository.save(room);
        bookingRepository.save(booking);
        return "redirect:/bookings";
    }

    @PostMapping("/checkin/{id}")
    public String checkIn(@PathVariable Long id) {
        Booking booking = bookingRepository.findById(id).orElseThrow();
        booking.setStatus("CHECKED_IN");
        bookingRepository.save(booking);
        return "redirect:/bookings";
    }

    @PostMapping("/checkout/{id}")
    public String checkOut(@PathVariable Long id) {
        Booking booking = bookingRepository.findById(id).orElseThrow();
        booking.setStatus("CHECKED_OUT");
        Room room = booking.getRoom();
        room.setRoomStatus("EMPTY");
        roomRepository.save(room);
        bookingRepository.save(booking);
        return "redirect:/bookings";
    }

    // Keep REST API endpoints
    @RestController
    @RequestMapping("/api/bookings")
    public static class BookingRestController {
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

        @GetMapping
        public List<Booking> getAll() {
            return bookingRepository.findAll();
        }
    }
}