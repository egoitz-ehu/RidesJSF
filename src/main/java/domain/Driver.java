package domain;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

@Entity
public class Driver extends User {
	@OneToMany(fetch = FetchType.LAZY, targetEntity = Ride.class, mappedBy = "driver", cascade=CascadeType.PERSIST)
	private List<Ride> rides;

	public List<Ride> getRides() {
		return rides;
	}

	public void setRides(List<Ride> rides) {
		this.rides = rides;
	}

	public boolean doesRideExists(String from, String to, Date date) {
		for (Ride r : rides)
			if ((java.util.Objects.equals(r.getDepartingCity(), from))
					&& (java.util.Objects.equals(r.getArrivalCity(), to))
					&& (java.util.Objects.equals(r.getRideDate(), date)))
				return true;

		return false;
	}
	
	public Ride addRide(String from, String to, Date date, int nPlaces, float price)  {
        Ride ride=new Ride(from,to,date,nPlaces,price, this);
        rides.add(ride);
        return ride;
	}
}
