package eredua.bean;

import java.io.Serializable;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named("manageReservationsTravelerBean")
@RequestScoped
public class ManageReservationsTravelerBean implements Serializable{
	@Inject
	private AuthBean authBean;
	
	@PostConstruct
	public void init() {
		
	}
}
