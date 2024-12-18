package com.servPet.pgBooking.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.servPet.pgBooking.model.PgBookingDTO;
import com.servPet.pgBooking.model.PgBookingService;

@Controller
@RequestMapping("/booking")
public class PgBookingController {

    @Autowired
    private PgBookingService pgBookingService;

    @GetMapping("/calendar")
    public String showCalendar(Model model,Principal principal) {
        List<PgBookingDTO> bookings = pgBookingService.getMemberBookings(principal);
        model.addAttribute("bookings", bookings);
        return "front_end/pg_schedule";
    }

    @PutMapping("/update/{pgoId}")
    @ResponseBody
    public ResponseEntity<?> updateBooking(@PathVariable Integer pgoId, 
    		@RequestParam String bookingDate, @RequestParam String bookingTime) {
    	

        try {
            pgBookingService.updateBooking(pgoId, bookingDate, bookingTime );
            return ResponseEntity.ok().build();
        } catch (Exception e) {
        	 System.out.println("Error message: " + e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/cancel/{pgoId}")
    @ResponseBody
    public ResponseEntity<?> cancelBooking(@PathVariable Integer pgoId,Principal principal) {
        try {
            pgBookingService.cancelBooking(pgoId,principal);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/rate/{pgoId}")
    @ResponseBody
    public ResponseEntity<?> rateBooking(@PathVariable Integer pgoId, 
                                        @RequestParam Integer star,
                                        @RequestParam String comment,
                                        Principal principal) {
        try {
            pgBookingService.addRating(pgoId, star, comment,principal);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}