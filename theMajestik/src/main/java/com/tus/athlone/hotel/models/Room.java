package com.tus.athlone.hotel.models;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

import org.hibernate.annotations.NaturalId;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.validation.constraints.NotNull;

@Entity(name = "rooms")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class Room {
	@Id
	@Column(unique = true)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	@ManyToOne
	private Hotel hotel;
	@NaturalId
	@Column(nullable = false, unique = true)
	private String roomNumber;
	@Column(nullable = false)
	private String roomType;
	@Column(nullable = false)
	private int beds;
	@Column(nullable = false)
	private BigDecimal costPerNight;
	private String description;
	private String available;
	private String image;
	@OneToMany(mappedBy = "room")
	private List<Booking> bookings;

	
	
	
	public String getAvailable() {
		return available;
	}

	public void setAvailable(String available) {
		this.available = available;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public BigDecimal getCostPerNight() {
		return costPerNight;
	}

	public void setCostPerNight(BigDecimal costPerNight) {
		this.costPerNight = costPerNight;
	}


	public Room() {

	}

	public Room(String roomNumber, String roomType, int beds, BigDecimal costPerNight, String description,
			String image, String available) {
		super();
		this.roomNumber = roomNumber;
		this.roomType = roomType;
		this.beds = beds;
		this.costPerNight = costPerNight;
		this.description = description;
		this.image = image;
		this.available = available;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Hotel getHotel() {
		return hotel;
	}

	public void setHotel(Hotel hotel) {
		this.hotel = hotel;
	}

	public String getRoomNumber() {
		return roomNumber;
	}

	public void setRoomNumber(String roomNumber) {
		this.roomNumber = roomNumber;
	}

	public String getRoomType() {
		return roomType;
	}

	public void setRoomType(String roomType) {
		this.roomType = roomType;
	}

	public int getBeds() {
		return beds;
	}

	public void setBeds(int beds) {
		this.beds = beds;
	}

	public List<Booking> getBookings() {
		return bookings;
	}

	public void setBooking(Booking booking) {
		if (booking != null) {
			booking.setRoom(this);
			bookings.add(booking);
			
		}
	}

	public boolean isReserved() {
		boolean booking;
		if (bookings.size() >0) 
			return booking = true;
		return booking = false;

	}

	@Override
	public int hashCode() {
		return Objects.hash(available, beds, bookings, costPerNight, description, hotel, id, image, roomNumber,
				roomType);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Room other = (Room) obj;
		return Objects.equals(available, other.available) && beds == other.beds
				&& Objects.equals(bookings, other.bookings) && Objects.equals(costPerNight, other.costPerNight)
				&& Objects.equals(description, other.description) && Objects.equals(hotel, other.hotel)
				&& Objects.equals(id, other.id) && Objects.equals(image, other.image)
				&& Objects.equals(roomNumber, other.roomNumber) && roomType == other.roomType;
	}

	@Override
	public String toString() {
		return "Room [id=" + id + ", hotel=" + hotel + ", roomNumber=" + roomNumber + ", roomType=" + roomType
				+ ", beds=" + beds + ", costPerNight=" + costPerNight + ", description=" + description + ", available="
				+ available + ", image=" + image + ", booking=" + bookings + "]";
	}

}
