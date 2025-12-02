package eredua.bean;

import java.io.Serializable;
import java.util.Date;
import java.util.ResourceBundle;

import org.primefaces.event.FlowEvent;

import exceptions.RideAlreadyExistException;
import exceptions.RideMustBeLaterThanTodayException;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.validator.ValidatorException;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named("rideBean")
@SessionScoped
public class RideBean implements Serializable {
	@Inject
	private AuthBean authBean;
	
	private String departingCity;
	private String arrivalCity;
	private int seats;
	private double price;
	private Date rideDate;

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

	public int getSeats() {
		return seats;
	}

	public void setSeats(int seats) {
		this.seats = seats;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public Date getRideDate() {
		return rideDate;
	}

	public void setRideDate(Date rideDate) {
		this.rideDate = rideDate;
	}

	public String onFlowProcess(FlowEvent event) {
		return event.getNewStep();
	}
	
	public void validateRideDate(FacesContext context, UIComponent component, Object value) {
	    Date selectedDate = (Date) value;
	    if (selectedDate == null || selectedDate.before(new Date())) {
	        ResourceBundle bundle = ResourceBundle.getBundle("messages", FacesContext.getCurrentInstance().getViewRoot().getLocale());
	        String msg = bundle.getString("createRide.errorDate");
	        FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, null);
	        throw new ValidatorException(facesMessage);
	    }
	}
	
	public void createRide() {
		try {
			String email = authBean.getUser().getEmail();
			FacadeBean.getBusinessLogic().createRide(departingCity, arrivalCity, rideDate, seats, price, email);
			ResourceBundle bundle = ResourceBundle.getBundle("messages", FacesContext.getCurrentInstance().getViewRoot().getLocale());
	        String msg = bundle.getString("createRide.rideCreatedMessage");
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "", msg));
		} catch (RideMustBeLaterThanTodayException | RideAlreadyExistException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
