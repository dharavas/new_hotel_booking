package com.tus.athlone.hotel.services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.tus.athlone.hotel.models.Booking;
import com.tus.athlone.hotel.models.Room;
import com.tus.athlone.hotel.repositories.RoomRepository;

@Service
public class RoomService {
    @Autowired
    private RoomRepository roomRepository;

    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    public Room getRoomById(Long id) {
        return roomRepository.findById(id).orElse(null);
    }
    
    public void removeRoomById(Long id) {
    		roomRepository.deleteById(id);
    }
    
    public Room addRoom(@RequestBody final Room room) {
        return roomRepository.saveAndFlush(room);
    }
    
    public List<Room> findAvailableRooms(LocalDate checkinDate, LocalDate checkoutDate, int guests){
    	List<Room> availableRooms = new ArrayList<>();
    	
    	//Get all rooms
    	  List<Room> allRooms = roomRepository.findAll();
    	  
    	  // Loop through each room
    	  
    	  for (Room room : allRooms) {
    		  boolean isAvailable = true;
    	  
    		  List<Booking> reservations = room.getBookings();

    		  for (Booking reservation : reservations) {
    			  System.out.println("Payment ID id: " + reservation.getPaymentId() );
    		      if (reservation.getPaymentId() != null && reservation.getCheckInDate().isBefore(checkoutDate) && reservation.getCheckOutDate().isAfter(checkinDate)) {
    		        // Room is already reserved during the selected dates
    		        isAvailable = false;
    		        break;
    		      }
    		    }
    		    if (isAvailable && room.getBeds() >= guests) {
    		      // Room is available and has enough capacity for the selected number of guests
    		      availableRooms.add(room);
    		    }
    		
    }
    		return availableRooms;
}
}
