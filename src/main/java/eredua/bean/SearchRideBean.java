package eredua.bean;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import org.primefaces.event.DateViewChangeEvent;
import org.primefaces.event.SelectEvent;

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
	
	private Date currentViewDate;

	public void init() {
		this.departingCities = FacadeBean.getBusinessLogic().getDepartCities();
		currentViewDate = new Date();
	}
	
	public void onDepartingCityChange() {
		this.arrivalCities = FacadeBean.getBusinessLogic().getDestinationCities(departingCity);
		System.out.println("Arrival cities updated: " + arrivalCities);
		checkAndLoadAvailableDates();
	}
	
	public void onArrivalCityChange() {
		checkAndLoadAvailableDates();
	}
	
	
	public void searchRides() {
		this.results = FacadeBean.getBusinessLogic().getRides(departingCity, arrivalCity, rideDate);
		if(this.results.isEmpty()) {
			ResourceBundle bundle = ResourceBundle.getBundle("messages", FacesContext.getCurrentInstance().getViewRoot().getLocale());
			String msgBody = bundle.getString("fetchRide.noResults");
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, null, msgBody));
		}
	}
	
	public void onMonthChange(DateViewChangeEvent event) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, event.getYear());
		cal.set(Calendar.MONTH, event.getMonth()-1);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		
		this.currentViewDate = cal.getTime();
		
		System.out.println("=== Hilabete aldaketa ===");
		System.out.println("Hilabetea: " + (event.getMonth() + 1));
		System.out.println("Urtea: " + event.getYear());
		System.out.println("Departing city: " + departingCity);
		System.out.println("Arrival city: " + arrivalCity);
		
		checkAndLoadAvailableDates();
	}
	
	private void checkAndLoadAvailableDates() {
		if (departingCity != null && !departingCity.isEmpty() 
				&& arrivalCity != null && !arrivalCity.isEmpty()) {
			
			Date dateToCheck = (currentViewDate != null) ? currentViewDate : new Date();
			
			List<Date> availableDates = FacadeBean.getBusinessLogic().getThisMonthDatesWithRides(departingCity, arrivalCity, dateToCheck);
			
			// Debug info
			System.out.println("=== Datak ===");
			if (availableDates != null && !availableDates.isEmpty()) {
				System.out.println(availableDates.size() + " data aurkitu dira:");
				availableDates.forEach(date -> System.out.println("  - " + date));
			} else {
				System.out.println("No se encontraron fechas disponibles");
			}
			
			// TODO: Más adelante aquí procesarás las fechas para colorear el calendario
		} else {
			System.out.println("=== Datuak falta ===");
			System.out.println("Departing city: " + (departingCity != null ? departingCity : "ez aukeratu"));
			System.out.println("Arrival city: " + (arrivalCity != null ? arrivalCity : "ez aukeratu"));
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
