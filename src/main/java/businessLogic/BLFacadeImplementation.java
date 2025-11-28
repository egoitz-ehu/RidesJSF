package businessLogic;

import java.util.Date;
import java.util.List;

import database.DataAccessMaria;
import domain.Ride;
import exceptions.RideAlreadyExistException;
import exceptions.RideMustBeLaterThanTodayException;

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
	public void createRide(String departingCity, String arrivalCity, Date rideDate, int nPlaces, double price,
			String driverEmail)  throws RideAlreadyExistException, RideMustBeLaterThanTodayException {
		// TODO Auto-generated method stub
		
	}
}
