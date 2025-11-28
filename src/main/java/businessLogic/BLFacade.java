package businessLogic;

import java.util.Date;
import java.util.List;

import domain.Ride;
import exceptions.RideAlreadyExistException;
import exceptions.RideMustBeLaterThanTodayException;

public interface BLFacade {

	public List<String> getDepartCities();

	public List<String> getDestinationCities(String departingCity);

	public List<Ride> getRides(String departingCity, String arrivalCity, Date rideDate);

	public List<Date> getThisMonthDatesWithRides(String from, String to, Date date);

	public Ride createRide(String departingCity, String arrivalCity, Date rideDate, int nPlaces, double price,
			String driverEmail) throws RideAlreadyExistException, RideMustBeLaterThanTodayException;
}
