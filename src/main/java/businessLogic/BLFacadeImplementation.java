package businessLogic;

import java.util.Date;
import java.util.List;

import database.DataAccessMaria;
import domain.Ride;
import domain.User;
import exceptions.RideAlreadyExistException;
import exceptions.RideMustBeLaterThanTodayException;
import exceptions.UserAlreadyRegistered;

public class BLFacadeImplementation implements BLFacade {
	private DataAccessMaria dataAccess;

	public BLFacadeImplementation(DataAccessMaria dataAccess) {
		this.dataAccess = dataAccess;
	}

	@Override
	public List<String> getDepartCities() {
		dataAccess.open();
		List<String> departCities = dataAccess.getDepartCities();
		dataAccess.close();
		return departCities;
	}

	@Override
	public List<String> getDestinationCities(String departingCity) {
		dataAccess.open();
		List<String> arrivalCities = dataAccess.getArrivalCities(departingCity);
		dataAccess.close();
		return arrivalCities;
	}

	@Override
	public List<Ride> getRides(String departingCity, String arrivalCity, Date rideDate) {
		dataAccess.open();
		List<Ride> rides = dataAccess.getRidesByValues(departingCity, arrivalCity, rideDate);
		dataAccess.close();
		return rides;
	}

	@Override
	public List<Date> getThisMonthDatesWithRides(String from, String to, Date date) {
		dataAccess.open();
		List<Date> dates = dataAccess.getThisMonthDatesWithRides(from, to, date);
		dataAccess.close();
		return dates;
	}

	@Override
	public Ride createRide(String departingCity, String arrivalCity, Date rideDate, int nPlaces, double price,
			String driverEmail) throws RideAlreadyExistException, RideMustBeLaterThanTodayException {
		dataAccess.open();
		Ride r = dataAccess.createRide(departingCity, arrivalCity, rideDate, nPlaces, nPlaces, driverEmail);
		dataAccess.close();
		return r;
	}

	@Override
	public User register(String email, String name, String password, boolean isDriver) throws UserAlreadyRegistered {
		dataAccess.open();
		try {
			User u = dataAccess.register(email, name, password, isDriver);
			dataAccess.close();
			return u;
		} catch(UserAlreadyRegistered e) {
			dataAccess.close();
			throw e;
		}
	}

	@Override
	public User login(String email, String password) {
		dataAccess.open();
		User u = dataAccess.login(email, password);
		dataAccess.close();
		return u;
	}
}
