package eredua.bean;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import org.primefaces.event.DateViewChangeEvent;

import domain.Ride;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;

@Named("searchRideBean")
@SessionScoped
public class SearchRideBean implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private String departingCity;
	private String arrivalCity;
	private Date rideDate;
	private List<Ride> results;

	private List<String> departingCities;
	private List<String> arrivalCities;
	
	private Date currentViewDate;
	private String availableDatesStr;
	
	private boolean initialized = false;

	public void init() {
		this.departingCities = FacadeBean.getBusinessLogic().getDepartCities();
		if (initialized) {
			return;
		}
		this.currentViewDate = new Date();
		this.availableDatesStr = "";
		initialized = true;
		System.out.println("=== INIT ===");
	}
	
	public void onDepartingCityChange() {
		System.out.println("\n=== onDepartingCityChange ===");
		this.arrivalCity = "";
		this.availableDatesStr = "";
		this.arrivalCities = FacadeBean.getBusinessLogic().getDestinationCities(departingCity);
		System.out.println("Arrival cities: " + (arrivalCities != null ? arrivalCities.size() : 0));
	}
	
	public void onArrivalCityChange() {
		System.out.println("\n=== onArrivalCityChange ===");
		checkAndLoadAvailableDates();
	}
	
	public void searchRides() {
		this.results = FacadeBean.getBusinessLogic().getRides(departingCity, arrivalCity, rideDate);
		if(this.results == null || this.results.isEmpty()) {
			ResourceBundle bundle = ResourceBundle.getBundle("messages", 
				FacesContext.getCurrentInstance().getViewRoot().getLocale());
			String msgBody = bundle.getString("fetchRide.noResults");
			FacesContext.getCurrentInstance().addMessage(null, 
				new FacesMessage(FacesMessage.SEVERITY_INFO, null, msgBody));
		}
	}
	
	public void onMonthChange(DateViewChangeEvent event) {
		System.out.println("\n=== onMonthChange ===");
		
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, event.getYear());
		cal.set(Calendar.MONTH, event.getMonth()-1);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		
		this.currentViewDate = cal.getTime();
		
		System.out.println("Mes: " + event.getMonth() + ", Año: " + event.getYear());
		System.out.println("Current view: " + currentViewDate);
		
		checkAndLoadAvailableDates();
	}
	
	private void checkAndLoadAvailableDates() {
		if (departingCity != null && !departingCity.isEmpty() 
				&& arrivalCity != null && !arrivalCity.isEmpty()) {
			
			Date dateToCheck = (currentViewDate != null) ? currentViewDate : new Date();
			System.out.println("Aztertzen den data: " + dateToCheck);
			
			List<Date> availableDates = FacadeBean.getBusinessLogic()
					.getThisMonthDatesWithRides(departingCity, arrivalCity, dateToCheck);
			
			if (availableDates == null) {
				availableDates = new ArrayList<>();
			}
			
			System.out.println("=== Datak ===");
			System.out.println(availableDates.size() + " data aurkitu dira:");
			availableDates.forEach(date -> System.out.println("  ✓ " + date));
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			this.availableDatesStr = availableDates.stream()
					.map(date -> sdf.format(date))
					.collect(Collectors.toList()).toString();
			
		System.out.println("Available dates str: " + availableDatesStr);
			
		} else {
			System.out.println("=== Datuak falta ===");
			System.out.println("Departing: " + departingCity);
			System.out.println("Arrival: " + arrivalCity);
			this.availableDatesStr = "";
		}
	}
	

	// Getters y Setters
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

	public Date getCurrentViewDate() {
		return currentViewDate;
	}

	public void setCurrentViewDate(Date currentViewDate) {
		this.currentViewDate = currentViewDate;
	}

	public String getAvailableDatesStr() {
		return availableDatesStr;
	}

	public void setAvailableDatesStr(String availableDatesStr) {
		this.availableDatesStr = availableDatesStr;
	}
	
	
}