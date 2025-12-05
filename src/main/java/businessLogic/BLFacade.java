package businessLogic;

import java.util.Date;
import java.util.List;

import domain.Reservation;
import domain.Ride;
import domain.Transfer;
import domain.User;
import exceptions.NotAvailableSeatsException;
import exceptions.NotEnoughMoneyException;
import exceptions.RideAlreadyExistException;
import exceptions.RideMustBeLaterThanTodayException;
import exceptions.UserAlreadyRegistered;

public interface BLFacade {

	public List<String> getDepartCities();

	public List<String> getDestinationCities(String departingCity);

	public List<Ride> getRides(String departingCity, String arrivalCity, Date rideDate);

	public List<Date> getThisMonthDatesWithRides(String from, String to, Date date);

	public Ride createRide(String departingCity, String arrivalCity, Date rideDate, int nPlaces, double price,
			String driverEmail) throws RideAlreadyExistException, RideMustBeLaterThanTodayException;
	
	public User register(String email, String name, String password, boolean isDriver) throws UserAlreadyRegistered;
	
	public User login(String email, String password);
	
	public Reservation createReservation(Date createDate, long rideId, String travelerEmail, int places) throws NotAvailableSeatsException, NotEnoughMoneyException;

	public void depositMoney(String userEmail, double amount);
	
	public boolean withdrawMoney(String userEmail, double amount) throws NotEnoughMoneyException;
	
	public double getUserBalance(String userEmail);
	
	public List<Transfer> getUserTransfers(String userEmail);
}
