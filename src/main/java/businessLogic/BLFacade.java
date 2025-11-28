package businessLogic;

import java.util.Date;
import java.util.List;

import domain.Ride;

public interface BLFacade {
	public List<Ride> getThisMonthDatesWithRides(String departingCity, String arrivalCity, Date rideDate);
}
