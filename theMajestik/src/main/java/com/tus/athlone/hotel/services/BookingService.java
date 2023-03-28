package com.tus.athlone.hotel.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tus.athlone.hotel.models.Booking;
import com.tus.athlone.hotel.models.Room;
import com.tus.athlone.hotel.repositories.BookingRepository;

@Service
public class BookingService {
    @Autowired
    private BookingRepository bookingRepository;
    
    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }


    public List<Booking> findByRoom(Room room) {
        return bookingRepository.findByRoom(room);
    }
    public Booking getBookingById(Long id) {
        return bookingRepository.findById(id).orElse(null);
    }
    
    public void removeBookingById(Long id) {
		bookingRepository.deleteById(id);
}

    
    public Booking saveBooking(Booking booking) {
        bookingRepository.save(booking);
        return booking;
        
    }
    }
