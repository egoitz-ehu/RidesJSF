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

import org.primefaces.PrimeFaces;
import org.primefaces.event.DateViewChangeEvent;

import domain.Reservation;
import domain.Ride;
import exceptions.NotAvailableSeatsException;
import exceptions.NotEnoughMoneyException;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named("searchRideBean")
@SessionScoped
public class SearchRideBean implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Inject
	private AuthBean authBean;

	private String departingCity;
	private String arrivalCity;
	private Date rideDate;
	private List<Ride> results;

	private List<String> departingCities;
	private List<String> arrivalCities;

	private Date currentViewDate;
	private String availableDatesStr;

	private boolean initialized = false;

	private Ride selectedRide;
	private int numberOfSeats = 1;
	private double totalPrice = 0.0;

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
		if (this.results == null || this.results.isEmpty()) {
			ResourceBundle bundle = ResourceBundle.getBundle("messages",
					FacesContext.getCurrentInstance().getViewRoot().getLocale());
			String msgBody = bundle.getString("fetchRide.noResults");
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, null, msgBody));
		}
	}

	public void onMonthChange(DateViewChangeEvent event) {
		System.out.println("\n=== onMonthChange ===");
		System.out.println(event.getMonth());

		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, event.getYear());
		cal.set(Calendar.MONTH, event.getMonth() - 1);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);

		this.currentViewDate = cal.getTime();

		checkAndLoadAvailableDates();
	}

	private void checkAndLoadAvailableDates() {
		if (departingCity != null && !departingCity.isEmpty() && arrivalCity != null && !arrivalCity.isEmpty()) {

			//Date dateToCheck = (currentViewDate != null) ? currentViewDate : new Date();

			List<Date> availableDates = FacadeBean.getBusinessLogic().getThisMonthDatesWithRides(departingCity,
					arrivalCity);

			if (availableDates == null) {
				availableDates = new ArrayList<>();
			}

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			this.availableDatesStr = availableDates.stream().map(date -> sdf.format(date)).collect(Collectors.toList())
					.toString();
			PrimeFaces.current().ajax().update("searchForm:activeDatesData");
		} else {
			this.availableDatesStr = "";
		}
	}

	public void prepareBooking(Ride ride) {
		this.selectedRide = ride;
		this.numberOfSeats = 1;
		calculateTotal();
	}

	public void calculateTotal() {
		if (selectedRide != null) {
			this.totalPrice = selectedRide.getPricePerSeat() * numberOfSeats;
		}
	}

	public void confirmBooking() {
		FacesContext context = FacesContext.getCurrentInstance();
		ResourceBundle bundle = ResourceBundle.getBundle("messages", context.getViewRoot().getLocale());
		boolean success = false;

		try {
			Reservation reservation = FacadeBean.getBusinessLogic().createReservation(new Date(),
					this.selectedRide.getId(), authBean.getUser().getEmail(), numberOfSeats);
			if(reservation == null) {
				String msgBody = bundle.getString("booking.error");
				context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, null, msgBody));
			} else {
				String msgBody = bundle.getString("booking.success");
				context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, null, msgBody));
				success = true;
				this.selectedRide.setAvailableSeats(this.selectedRide.getAvailableSeats() - numberOfSeats);
			}
		} catch(NotEnoughMoneyException e) {
			String msgBody = bundle.getString("booking.error.notEnoughMoney");
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, null, msgBody));
		} catch(NotAvailableSeatsException e) {
			String msgBody = bundle.getString("booking.error.notAvailableSeats");
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, null, msgBody));
		}
		this.selectedRide = null;
		PrimeFaces.current().ajax().addCallbackParam("success", success);
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

	public int getNumberOfSeats() {
		return numberOfSeats;
	}

	public void setNumberOfSeats(int numberOfSeats) {
		this.numberOfSeats = numberOfSeats;
	}

	public double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}

	public Ride getSelectedRide() {
		return selectedRide;
	}

	public void setSelectedRide(Ride selectedRide) {
		this.selectedRide = selectedRide;
	}

}