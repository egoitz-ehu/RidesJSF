package domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

@Entity
public class Traveler extends User {
	@OneToMany(fetch = FetchType.LAZY, targetEntity = Reservation.class, mappedBy = "traveler")
	private List<Reservation> reservations;
	
	public Traveler() {
		super();
	}
	
	public Traveler(String email, String name, String password) {
		super(email, name, password);
		this.reservations = new ArrayList<Reservation>();
	}

	public List<Reservation> getReservations() {
		return reservations;
	}

	public void setReservations(List<Reservation> reservations) {
		this.reservations = reservations;
	}
	
	public void addReservation(Reservation reservation) {
		this.reservations.add(reservation);
	}
}
