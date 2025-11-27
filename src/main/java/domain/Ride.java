package domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Ride {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String departingCity;
	private String arrivalCity;
	private Date rideDate;
	private int availableSeats;
	private double pricePerSeat;

	public Ride() {
	}
	
	public Ride(String departingCity, String arrivalCity, Date rideDate, int availableSeats, double pricePerSeat) {
		this.departingCity = departingCity;
		this.arrivalCity = arrivalCity;
		this.rideDate = rideDate;
		this.availableSeats = availableSeats;
		this.pricePerSeat = pricePerSeat;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDepartingCity() {
		return departingCity;
	}

	public void setDepartingCity(String departingCity) {
		this.departingCity = departingCity;
	}

	public String getArrivalCity() {
		return arrivalCity;
	}

	public void setArrivalCity(String arrivalCity) {
		this.arrivalCity = arrivalCity;
	}

	public Date getRideDate() {
		return rideDate;
	}

	public void setRideDate(Date rideDate) {
		this.rideDate = rideDate;
	}

	public int getAvailableSeats() {
		return availableSeats;
	}

	public void setAvailableSeats(int availableSeats) {
		this.availableSeats = availableSeats;
	}

	public double getPricePerSeat() {
		return pricePerSeat;
	}

	public void setPricePerSeat(double pricePerSeat) {
		this.pricePerSeat = pricePerSeat;
	}
}
