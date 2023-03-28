package com.tus.athlone.hotel.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.paypal.base.rest.PayPalRESTException;
import com.tus.athlone.hotel.models.Booking;
import com.tus.athlone.hotel.models.BookingDetail;
import com.tus.athlone.hotel.models.Room;
import com.tus.athlone.hotel.repositories.BookingRepository;

import com.tus.athlone.hotel.repositories.RoomRepository;
import com.tus.athlone.hotel.services.PaymentServices;

@RestController
@RequestMapping("/app/v1/bookings")
public class BookingsController {
	
	 private RoomRepository roomRepository;

		@Autowired
	    private BookingRepository bookingRepository;
	    

	    // Flow step 1
	    @GetMapping("/reservation")
	    public String getDateForm(@RequestParam(value = "roomId") Long roomId)
	            throws NotFoundException {

	        Optional<Room> maybeRoom = roomRepository.findById(roomId);
	        if (!maybeRoom.isPresent()) {
	            throw new NotFoundException();
	        }

	        return "booking/dates";
	    }
	    
	

	
	
	@GetMapping
	public List<Booking> list(){
		return bookingRepository.findAll();
	}
	
	@SuppressWarnings("deprecation")
	@GetMapping
	@RequestMapping("{id}")
	public Booking get(@PathVariable Long id){
		return bookingRepository.getOne(id);
	}
	
	@PostMapping
	public Booking create(@RequestBody final Booking booking) {
		return bookingRepository.saveAndFlush(booking);
	}
	
	@RequestMapping(value = "{id}", method = RequestMethod.DELETE)
	public void delete(@PathVariable Long id) {
		bookingRepository.deleteById(id);
	}
	
	@RequestMapping(value = "{id}", method = RequestMethod.PUT)
	public Booking update(@PathVariable Long id, @RequestBody final Booking booking) {
		
		Booking existingbooking = bookingRepository.getById(id);
		BeanUtils.copyProperties(booking, existingbooking, "id");
		return bookingRepository.saveAndFlush(existingbooking);
	}
	
	
	@PostMapping("/payment/{bookingId}")
    public String showPaymentForm(@PathVariable("bookingId") Long bookingId, RedirectAttributes redirectAttributes) {
        Booking booking = bookingRepository.getById(bookingId);
        try {
        	BookingDetail bookingDetail = new BookingDetail(booking.getBookingId(),booking.getReservationId(), booking.getRoom().getRoomType(), booking.getTotalRoomCost(), booking.getTaxableAmount());
            PaymentServices paymentServices = new PaymentServices();
            String approvalLink = paymentServices.authorizePayment(bookingDetail);
            System.out.println(approvalLink);
            return approvalLink;
        } catch (PayPalRESTException ex) {
        	System.out.println("Error removing room: " + ex);
        	
        	return "error.html";
        }
    }
}


