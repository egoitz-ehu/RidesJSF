package eredua.bean;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import domain.Reservation;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;

@Named("manageReservationsDriverBean")
@RequestScoped
public class ManageReservationsDriverBean implements Serializable{
	private List<Reservation> reservations;
	
	@PostConstruct
	public void init() {
		this.reservations = new LinkedList<Reservation>();
	}

	public List<Reservation> getReservations() {
		return reservations;
	}

	public void setReservations(List<Reservation> reservations) {
		this.reservations = reservations;
	}
	
	
}
