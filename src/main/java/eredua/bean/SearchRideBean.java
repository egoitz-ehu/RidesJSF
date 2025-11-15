package eredua.bean;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import domain.Ride;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;

@Named("searchRideBean")
@SessionScoped
public class SearchRideBean implements Serializable{
	private String departingCity;
	private String arrivalCity;
	private Date rideDate;
	private List<Ride> results;

	private List<String> departingCities;
	private List<String> arrivalCities;

	public void init() {
		this.departingCities = FacadeBean.getBusinessLogic().getDepartCities();
	}
	
	public void onDepartingCityChange() {
		this.arrivalCities = FacadeBean.getBusinessLogic().getDestinationCities(departingCity);
	}
	
	public void searchRides() {
		this.results = FacadeBean.getBusinessLogic().getRides(departingCity, arrivalCity, rideDate);
		if(this.results.isEmpty()) {
			ResourceBundle bundle = ResourceBundle.getBundle("messages", FacesContext.getCurrentInstance().getViewRoot().getLocale());
			String msgBody = bundle.getString("fetchRide.noResults");
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, null, msgBody));
		}
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

	public List<Ride> getResults() {
		return results;
	}

	public void setResults(List<Ride> results) {
		this.results = results;
	}

	public List<String> getDepartingCities() {
		return departingCities;
	}

	public void setDepartingCities(List<String> departingCities) {
		this.departingCities = departingCities;
	}

	public List<String> getArrivalCities() {
		return arrivalCities;
	}

	public void setArrivalCities(List<String> arrivalCities) {
		this.arrivalCities = arrivalCities;
	}
}
