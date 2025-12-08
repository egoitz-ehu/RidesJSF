package eredua.bean;

import java.io.Serializable;
import java.util.List;

import domain.Reservation;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named("manageReservationsTravelerBean")
@RequestScoped
public class ManageReservationsTravelerBean implements Serializable{
	@Inject
	private AuthBean authBean;
	
	private List<Reservation> reservations;
	
	@PostConstruct
	public void init() {
		this.reservations = FacadeBean.getBusinessLogic().getTravelerReservations(authBean.getUser().getEmail());
	}

	public List<Reservation> getReservations() {
		return reservations;
	}

	public void setReservations(List<Reservation> reservations) {
		this.reservations = reservations;
	}
}
