package com.tus.athlone.hotel.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tus.athlone.hotel.models.Booking;
import com.tus.athlone.hotel.models.Room;


public interface BookingRepository extends JpaRepository<Booking, Long>{

	List<Booking> findByRoom(Room room);
	

}
