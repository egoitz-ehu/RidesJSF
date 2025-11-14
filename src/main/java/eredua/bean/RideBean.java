package eredua.bean;

import java.io.Serializable;
import java.util.Date;

import org.primefaces.event.FlowEvent;

import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;

@Named("rideBean")
@SessionScoped
public class RideBean implements Serializable{
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
}
