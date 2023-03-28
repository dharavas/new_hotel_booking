package com.tus.athlone.hotel.models;

import java.math.BigDecimal;
import java.util.UUID;

public class BookingDetail {
    private String roomType;
    private Long bookingId;
    private UUID reservationId;
 

	private BigDecimal totalRoomCost;
    private BigDecimal tax;
    private BigDecimal total;
 
    public BookingDetail(Long bookingId, UUID reservationId,String roomType, BigDecimal totalRoomCost,
    		BigDecimal tax) {
    	this.bookingId = bookingId;
    	this.reservationId = reservationId;
        this.roomType = roomType;
        this.totalRoomCost = totalRoomCost;
        this.tax = tax;
        this.total = totalRoomCost.add(tax) ;
    }
 
    public String getRoomType() {
        return roomType;
    }
 
    public String getTotalRoomCost() {
        return String.format("%.2f", totalRoomCost);
    }

 
    public Long getBookingId() {
		return bookingId;
	}

	public void setBookingId(Long bookingId) {
		this.bookingId = bookingId;
	}

	public String getTax() {
        return String.format("%.2f", tax);
    }
     
    public String getTotal() {
        return String.format("%.2f", total);
    }
    
    public UUID getReservationId() {
 		return reservationId;
 	}

 	public void setReservationId(UUID reservationId) {
 		this.reservationId = reservationId;
 	}
}
