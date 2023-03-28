package com.tus.athlone.hotel.controllers;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import com.paypal.base.rest.PayPalRESTException;
import com.tus.athlone.hotel.SQSClientConfiguration;
import com.tus.athlone.hotel.models.Booking;
import com.tus.athlone.hotel.models.BookingDetail;
import com.tus.athlone.hotel.models.Room;
import com.tus.athlone.hotel.models.User;
import com.tus.athlone.hotel.repositories.RoomRepository;
import com.tus.athlone.hotel.repositories.UserRepository;
import com.tus.athlone.hotel.services.BookingService;
import com.tus.athlone.hotel.services.PaymentServices;
import com.tus.athlone.hotel.services.RoomService;
import com.tus.athlone.hotel.services.UserService;



@Controller
public class WebControllers {
	
	 @Autowired
	    private RoomService roomService;
	 
	 @Autowired
	    private UserService userService;
	 
	 @Autowired
	    private PaymentServices paymentService;
	 @Autowired
	    private BookingService bookingService;
		@Autowired
		private RoomRepository roomRepository;
	
	@GetMapping("/")
	public String showHomePage(){
		return "index.html";
	}
	
	@GetMapping("/login")
	public String showLoginPage(){
		return "login.html";
	}
	



	@PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody User user) {
        User registeredUser = userService.register(user);
        return ResponseEntity.ok(registeredUser);
    }
	
	@PostMapping("/paymentButton")
	public String showPaymentButton(@ModelAttribute("booking") Booking booking, Model model){
		model.addAttribute("booking", booking);
		return "paymentButton.html";
	}

	
	
    @GetMapping("/showRooms")
    public String showRoomList(Model model) {
        List<Room> roomList = roomService.getAllRooms();
        System.out.println(roomList);
        model.addAttribute("roomList", roomList);
        return "room.html";
    } 
    
    @GetMapping("/showBookings")
    public String showBookingList(Model model) {
        List<Booking> bookingList = bookingService.getAllBookings();
        System.out.println(bookingList);
        model.addAttribute("bookingList", bookingList);
        return "bookingList.html";
    } 
    
    @GetMapping("/admin")
    public String showadminPanel() {
        return "admin.html";
    }

    
    @GetMapping("/error")
    public String showaerrorPage() {
        return "error.html";
    }
    
    

    @GetMapping("/success/{bookingId}")
    public String handleSuccess(@RequestParam("paymentId") String paymentId,
                                 @RequestParam("token") String token,
                                 @RequestParam("PayerID") String payerId,
                                 @PathVariable("bookingId") Long bookingId,
                                 Authentication authentication,
                                 Model model) {
        // do something with the payment information
    	Booking booking = bookingService.getBookingById(bookingId);
    	booking.setPaymentStatus(true);
    	booking.setPaymentId(paymentId);
    	booking.setPayerId(payerId);
    	bookingService.saveBooking(booking);
        model.addAttribute("paymentId", paymentId);
        model.addAttribute("payerId", payerId);
        model.addAttribute("reservationId", booking.getReservationId());
        model.addAttribute("roomType", booking.getRoom().getRoomType());
        model.addAttribute("roomNumber", booking.getRoom().getRoomNumber());
        model.addAttribute("firstName", booking.getFirstName());
        model.addAttribute("email", booking.getEmail());
        model.addAttribute("checkOutDate", booking.getCheckOutDate());
        model.addAttribute("booking", booking);
        System.out.println(model.getAttribute("reservationId"));
        return "success.html"; // return the name of your Thymeleaf template
    }

    @GetMapping("/manageRooms")
    public String manageRoom() {
        return "manageRooms.html";
    }  
    
    @GetMapping("/manageBookings")
    public String manageBooking() {
        return "manageBookings.html";
    }  
    
    
	  @ModelAttribute("room")
	  public Room room() {
	    return new Room();
	  }
	  

	
    @GetMapping("/addRoom")
    public String addRoom(Model model) {
        model.addAttribute("room", new Room());
        return "addRoom.html";
    } 

    
    @PostMapping("/room/add")
    public String addRoom(@ModelAttribute("room") Room room) {
    	roomService.addRoom(room);
      return "redirect:/manageRooms";
    }

    @GetMapping("/removeRoom")
    public String removeRoom(Model model) {
            List<Room> roomList = roomService.getAllRooms();
            System.out.println(roomList);
            model.addAttribute("roomList", roomList);
        return "removeRoom.html";
    } 
    
    @GetMapping("/cancelBooking")
    public String cancelBooking(Model model) {
            List<Booking> bookingList = bookingService.getAllBookings();
            System.out.println(bookingList);
            model.addAttribute("bookingList", bookingList);
        return "cancelBooking.html";
    } 

    
    @PostMapping("/room/remove/{id}")
    public String removeRoom(@PathVariable("id") Long id, Model model) {
    	try {
    		roomService.removeRoomById(id);
    		 return "redirect:/manageRooms";
    	}catch(Exception ex) {
    		 System.out.println("Error removing room: " + ex.getMessage());
    		 model.addAttribute("errorMessage", "An error occurred while removing the room.");
    		 return "redirect:/error";
    	}
    }
    
    @PostMapping("/booking/remove/{id}")
    public String removeBooking(@PathVariable("id") Long id, Model model) {
    	try {
    		bookingService.removeBookingById(id);
    		 return "redirect:/manageBookings";
    	}catch(Exception ex) {
    		 System.out.println("Error Cancelling: " + ex.getMessage());
    		 model.addAttribute("errorMessage", "An error occurred while cancelling booking.");
    		 return "redirect:/error";
    	}
    }
    
    
    @PostMapping("/room/booking/{id}")
    public String removebooking(@PathVariable("id") Long id, Model model) {
    	try {
    		bookingService.removeBookingById(id);
    		 return "redirect:/managebookings";
    	}catch(Exception ex) {
    		 System.out.println("Error removing room: " + ex.getMessage());
    		 model.addAttribute("errorMessage", "An error occurred while removing the room.");
    		 return "redirect:/error";
    	}
    }
    
    @GetMapping("/showAvailableRooms")
    public String showAvailableRoomList(Model model) {
        List<Room> availableRooms = (List<Room>) model.getAttribute("availableRooms");
        model.addAttribute("availableRoomList", availableRooms);
        return "availableRooms.html";
    }

    
    @PostMapping("/room/checkAvailability")
    public String checkAvailability(@RequestParam("checkinDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkinDate,
            @RequestParam("checkoutDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkoutDate,
            @RequestParam("guests") int guests,
            RedirectAttributes redirectAttributes) {
    	List<Room> availableRooms = roomService.findAvailableRooms(checkinDate, checkoutDate, guests);
    	System.out.println(availableRooms);
    	redirectAttributes.addFlashAttribute("availableRooms", availableRooms);
    	return "redirect:/showAvailableRooms";
    }
    
    @PostMapping("/payment/{bookingId}")
    public String showPaymentForm(@PathVariable("bookingId") Long bookingId, RedirectAttributes redirectAttributes, Model model) {
        Booking booking = bookingService.getBookingById(bookingId);
        try {
        	BookingDetail bookingDetail = new BookingDetail(booking.getBookingId(),booking.getReservationId(), booking.getRoom().getRoomType(), booking.getTotalRoomCost(), booking.getTaxableAmount());
            PaymentServices paymentServices = new PaymentServices();
            String approvalLink = paymentServices.authorizePayment(bookingDetail);
            SQSClientConfiguration snsClient = new SQSClientConfiguration();
//           sqs.SendSQSMessage(approvalLink, "customer_booking_confirmation");
//            PublishTextSMS.pubTextSMS(sns, approvalLink, "+353877069272")
            
            
            return "redirect:" + approvalLink;
        } catch (PayPalRESTException ex) {
        	System.out.println("Error removing room: " + ex);
        	model.addAttribute("errorMessage", ex);
        	return "error.html";
        }
    }
	
    @GetMapping("/success")
        public String showWelcomePage(Model model,Authentication authentication,  RedirectAttributes redirectAttributes) {
        	String email = authentication.getName();
            User user = userService.findByEmail(email);
            model.addAttribute("user", user);
            System.out.println(user);
            return "success.html";
        }
    
    
    @PostMapping("/bookRoom/form/{roomId}")
    public String bookRoom(@PathVariable  Long roomId,Model model) {
    	Room room1 = roomRepository.findById(roomId).orElseThrow(() -> new IllegalArgumentException("Invalid room ID"));
    	model.addAttribute("room", room1);
    	System.out.println("Room:" + room1);
    	Booking booking1 = new Booking();
    	System.out.println(booking1);
    	booking1.setRoom(room1);
    	System.out.println("Booking" + booking1);
    	System.out.println("roomID" + booking1.getRoom());
    	model.addAttribute("booking", booking1);
    
    	
        return "book.html";
    }
    
    @PostMapping("/bookRoom/submit")
    public String submitBookingForm(@ModelAttribute("booking") Booking booking, Model model) {

    	Room room1 = roomRepository.findById(booking.getRoom().getId()).orElseThrow();
    	booking.setRoom(room1);
    	
        LocalDate checkinDate = booking.getCheckInDate();
        LocalDate checkoutDate = booking.getCheckOutDate();
        int guests = booking.getGuests();
        System.out.println("Booking Room:" +room1 );
        
        // Check room availability
        List<Room> availableRooms = roomService.findAvailableRooms(checkinDate, checkoutDate, guests);
        System.out.println("Available Room:" + availableRooms);
        System.out.println("payment status: " + booking.getPaymentSatus());
        if ((!availableRooms.contains(room1) )&& ((booking.getPaymentSatus() == true))) {
            // Room is not available during the selected dates, show error message
        	model.addAttribute("errorMessage", "Room is not available during the selected dates, show error message.");
            return "error.html";
        }
     // Room is available, proceed with booking
        booking.setTotalCost(booking.getTotalRoomCost());
        bookingService.saveBooking(booking);
        return "bookingConfirmation.html";
    }
    
    
    

}