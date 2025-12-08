package domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

import util.UtilDate;

@Entity
public class Driver extends User {
	@OneToMany(fetch = FetchType.LAZY, targetEntity = Ride.class, mappedBy = "driver", cascade = CascadeType.PERSIST)
	private List<Ride> rides;

	public Driver() {
		super();
		this.rides = new ArrayList<Ride>();
	}

	public Driver(String email, String name, String password) {
		super(email, name, password);
		this.rides = new ArrayList<Ride>();
	}

	public List<Ride> getRides() {
		return rides;
	}

	public void setRides(List<Ride> rides) {
		this.rides = rides;
	}

	public boolean doesRideExists(String from, String to, Date date) {
		for (Ride r : rides)
			if (r.getDepartingCity().equals(from) && r.getArrivalCity().equals(to) && isSameDay(r.getRideDate(), date))
				return true;

		return false;
	}

	private boolean isSameDay(Date d1, Date d2) {
	    return UtilDate.trim(d1).equals(UtilDate.trim(d2));
	}

	public Ride addRide(String from, String to, Date date, int nPlaces, double price) {
		Ride ride = new Ride(from, to, date, nPlaces, price, this);
		rides.add(ride);
		return ride;
	}
}
