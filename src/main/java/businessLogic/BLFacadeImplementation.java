package businessLogic;

import java.util.Date;
import java.util.List;

import database.DataAccessMaria;
import domain.Ride;

public class BLFacadeImplementation implements BLFacade {
	private DataAccessMaria dataAccess;

	public BLFacadeImplementation(DataAccessMaria dataAccess) {
		this.dataAccess = dataAccess;
	}

	@Override
	public List<Ride> getThisMonthDatesWithRides(String departingCity, String arrivalCity, Date rideDate) {
		dataAccess.open();
		List<Ride> rides = dataAccess.getRidesByValues(departingCity, arrivalCity, rideDate);
		dataAccess.close();
		return rides;
	}

}
