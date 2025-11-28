package domain;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

@Entity
public class Driver extends User{
	@OneToMany(fetch=FetchType.LAZY, targetEntity=Ride.class ,mappedBy="driver")
	private List<Ride> rides;

	public List<Ride> getRides() {
		return rides;
	}

	public void setRides(List<Ride> rides) {
		this.rides = rides;
	}
}
