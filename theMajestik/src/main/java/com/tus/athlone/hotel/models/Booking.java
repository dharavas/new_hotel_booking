package com.tus.athlone.hotel.models;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.validation.constraints.NotNull;


import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;



@Entity(name = "bookings")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class Booking {

	public static final double TAX_AMOUNT = 0.10;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private UUID reservationId = UUID.randomUUID();

	@Column(nullable = false)
	@NotNull(message = "Check in date required")
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private LocalDate checkInDate;

	@Column(nullable = false)
	@NotNull(message = "Check out date required")
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private LocalDate checkOutDate;

	@ManyToOne
	@JoinColumn(name = "room_id")
	private Room room;

	
	private int guests;
	private BigDecimal totalCost;
	private String firstName;
	private String lastName;
	private String phoneNumber;
	private String email;
	private String payerId;
	private String paymentId;

	private boolean paymentStatus=false;

	@Column(nullable = false)
	private LocalDateTime createdTime;
	@PrePersist
    protected void onCreate() {
        createdTime = LocalDateTime.now();
    }
	

	
	public String getPayerId() {
		return payerId;
	}



	public void setPayerId(String payerId) {
		this.payerId = payerId;
	}



	public String getPaymentId() {
		return paymentId;
	}



	public void setPaymentId(String paymentId) {
		this.paymentId = paymentId;
	}



	public void setReservationId(UUID reservationId) {
		this.reservationId = reservationId;
	}



	public Booking() {

	}
	public LocalDateTime getCreatedTime() {
		return createdTime;
	}

	public BigDecimal getTotalCost() {
		return totalCost;
	}

	public void setTotalCost(BigDecimal totalCost) {
		this.totalCost = totalCost;
	}

	public void setRoom(Room room) {
		this.room = room;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Room getRoom() {
		return room;
	}

	public boolean getPaymentSatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(boolean payment) {
		
		this.paymentStatus = payment;
	}

	public void setCreatedTime(LocalDateTime createdTime) {
		this.createdTime = createdTime;
	}

	public LocalDate getCheckInDate() {
		return checkInDate;
	}

	public void setCheckInDate(LocalDate checkInDate) {
		this.checkInDate = checkInDate;
	}

	public LocalDate getCheckOutDate() {
		return checkOutDate;
	}

	public void setCheckOutDate(LocalDate checkOutDate) {
		this.checkOutDate = checkOutDate;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getGuests() {
		return guests;
	}

	public void setGuests(int guests) {
		this.guests = guests;
	}

	public long totalNights() {
		if (checkInDate == null || checkOutDate == null) {
			return 0;
		}
		return ChronoUnit.DAYS.between(checkInDate, checkOutDate);
	}

	public Optional<ValidationError> validate(LocalDate now) {
		if (checkInDate == null) {
			return Optional.of(new ValidationError("checkInDate.missing", "Missing check in date"));
		} else if (checkOutDate == null) {
			return Optional.of(new ValidationError("checkOutDate.missing", "Missing check out date"));
		} else if (checkInDate.isBefore(now)) {
			return Optional.of(new ValidationError("checkInDate.future", "Check in date must be in the future"));
		} else if (checkOutDate.isBefore(checkInDate)) {
			return Optional.of(
					new ValidationError("checkOutDate.afterCheckIn", "Check out date must occur after check in date"));
		} else if (totalNights() < 1) {
			// handles case where check in/out dates are the same.
			return Optional
					.of(new ValidationError("checkOutDate.minNights", "Reservation must be for at least 1 night"));
		}
		return Optional.empty();
	}

	public static class ValidationError {
		private String code;
		private String reason;

		public ValidationError(String code, String reason) {
			this.code = code;
			this.reason = reason;
		}

		public String getCode() {
			return code;
		}

		public String getReason() {
			return reason;
		}
	}



	public Long getBookingId() {
		return id;
	}

	public void setBookingId(Long bookingId) {
		this.id = bookingId;
	}

	public UUID getReservationId() {
		return reservationId;
	}



	public BigDecimal getTotalRoomCost() {
		long nights = totalNights();
		if (nights == 0) {
			return BigDecimal.ZERO;
		}
		return room.getCostPerNight().multiply(BigDecimal.valueOf(nights));
	}

	public BigDecimal getTaxableAmount() {
		return getTotalRoomCost().multiply(BigDecimal.valueOf(TAX_AMOUNT));
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Booking that = (Booking) o;
		return Objects.equals(reservationId, that.reservationId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(reservationId);
	}

}
