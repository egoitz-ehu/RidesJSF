package eredua.bean;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
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
	private List<LocalDate> disabledDates;

	public void init() {
		this.departingCities = FacadeBean.getBusinessLogic().getDepartCities();
		currentViewDate = new Date();
		disabledDates = new ArrayList<>();
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
		System.out.println("Hilabetea: " + (event.getMonth()));
		System.out.println("Urtea: " + event.getYear());
		System.out.println("Departing city: " + departingCity);
		System.out.println("Arrival city: " + arrivalCity);
		
		checkAndLoadAvailableDates();
	}
	
	private void checkAndLoadAvailableDates() {
		if (departingCity != null && !departingCity.isEmpty() 
				&& arrivalCity != null && !arrivalCity.isEmpty()) {
			
			Date dateToCheck = (currentViewDate != null) ? currentViewDate : new Date();
			System.out.println("Aztertzen den data: " + dateToCheck);
			
			List<Date> availableDates = FacadeBean.getBusinessLogic().getThisMonthDatesWithRides(departingCity, arrivalCity, dateToCheck);
			
			// Debug info
			System.out.println("=== Datak ===");
			if (availableDates != null && !availableDates.isEmpty()) {
				System.out.println(availableDates.size() + " data aurkitu dira:");
				availableDates.forEach(date -> System.out.println("  - " + date));
			} else {
				System.out.println("No se encontraron fechas disponibles");
			}
			
			this.disabledDates = convertToDisabledDates(availableDates, dateToCheck);
		} else {
			System.out.println("=== Datuak falta ===");
			System.out.println("Departing city: " + (departingCity != null ? departingCity : "ez aukeratu"));
			System.out.println("Arrival city: " + (arrivalCity != null ? arrivalCity : "ez aukeratu"));
		}
	}
	

	private List<LocalDate> convertToDisabledDates(List<Date> availableDatesList, Date referenceDate) {
		List<LocalDate> disabledList = new ArrayList<>();
		
		if (referenceDate == null) {
			referenceDate = new Date();
		}
		
		if (availableDatesList == null) {
			availableDatesList = new ArrayList<>();
		}
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(referenceDate);
		
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH);
		int maxDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		
		System.out.println("\n=== Calculando fechas deshabilitadas ===");
		System.out.println("Mes: " + (month + 1) + "/" + year + " - Días: " + maxDay);
		System.out.println("Fechas disponibles: " + availableDatesList.size());
		
		List<LocalDate> availableLocalDates = new ArrayList<>();
		for (Date date : availableDatesList) {
			LocalDate localDate = date.toInstant()
					.atZone(ZoneId.systemDefault())
					.toLocalDate();
			availableLocalDates.add(localDate);
			System.out.println("  Disponible: " + localDate);
		}
		
		for (int day = 1; day <= maxDay; day++) {
			LocalDate currentDate = LocalDate.of(year, month + 1, day);
			
			if (!availableLocalDates.contains(currentDate)) {
				disabledList.add(currentDate);
				System.out.println("  ✗ Deshabilitado: " + currentDate);
			}
		}
		
		System.out.println("Total deshabilitadas: " + disabledList.size());
		return disabledList;
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

	public Date getCurrentViewDate() {
		return currentViewDate;
	}

	public void setCurrentViewDate(Date currentViewDate) {
		this.currentViewDate = currentViewDate;
	}

	public List<LocalDate> getDisabledDates() {
		return disabledDates;
	}

	public void setDisabledDates(List<LocalDate> disabledDates) {
		this.disabledDates = disabledDates;
	}
	
	
}
