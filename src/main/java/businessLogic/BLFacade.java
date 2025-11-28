package businessLogic;

import java.util.Date;
import java.util.List;

import domain.Ride;

public interface BLFacade {
	
	public List<String> getDepartCities();
	
	public List<String> getDestinationCities(String departingCity);
	
	public List<Ride> getRides(String departingCity, String arrivalCity, Date rideDate);
}
