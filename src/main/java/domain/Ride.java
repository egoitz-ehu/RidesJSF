package domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

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

	@ManyToOne(fetch = FetchType.EAGER)
	private Driver driver;

	@OneToMany(fetch = FetchType.LAZY, targetEntity = Reservation.class, mappedBy = "ride", cascade = CascadeType.PERSIST)
	private List<Reservation> reservations;

	@OneToMany(fetch = FetchType.LAZY, targetEntity = Transfer.class, mappedBy = "ride")
	private List<Transfer> transferList;

	public Ride() {
	}

	public Ride(String departingCity, String arrivalCity, Date rideDate, int availableSeats, double pricePerSeat,
			Driver driver) {
		this.departingCity = departingCity;
		this.arrivalCity = arrivalCity;
		this.rideDate = rideDate;
		this.availableSeats = availableSeats;
		this.pricePerSeat = pricePerSeat;
		this.driver = driver;
		this.reservations = new ArrayList<Reservation>();
		this.transferList = new ArrayList<Transfer>();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof Ride))
			return false;
		Ride other = (Ride) o;
		return id != null && id.equals(other.id);
	}

	@Override
	public int hashCode() {
		return id != null ? id.hashCode() : 0;
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

	public Driver getDriver() {
		return driver;
	}

	public void setDriver(Driver driver) {
		this.driver = driver;
	}

	public List<Reservation> getReservations() {
		return reservations;
	}

	public void setReservations(List<Reservation> reservations) {
		this.reservations = reservations;
	}

	public List<Transfer> getTransferList() {
		return transferList;
	}

	public void setTransferList(List<Transfer> transferList) {
		this.transferList = transferList;
	}

	public Reservation createReservation(Date createDate, Traveler t, int nPlaces) {
		double totalPrice = nPlaces * pricePerSeat;
		Reservation r = new Reservation(createDate, this, t, nPlaces, totalPrice);
		this.reservations.add(r);
		this.availableSeats -= nPlaces;
		t.moveMoneyToFrozen(totalPrice);
		return r;
	}
}
