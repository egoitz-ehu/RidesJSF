package businessLogic;

import java.util.Date;
import java.util.List;

import dataAccess.HibernateDataAccess;
import domain.Reservation;
import domain.Ride;
import domain.Transfer;
import domain.User;
import exceptions.NotAvailableSeatsException;
import exceptions.NotEnoughMoneyException;
import exceptions.RideAlreadyExistException;
import exceptions.RideMustBeLaterThanTodayException;
import exceptions.UserAlreadyRegistered;
import util.HashUtil;

public class BLFacadeImplementation implements BLFacade {
	private HibernateDataAccess dataAccess;

	public BLFacadeImplementation(HibernateDataAccess dataAccess) {
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
	public List<Date> getThisMonthDatesWithRides(String from, String to) {
		dataAccess.open();
		List<Date> dates = dataAccess.getThisMonthDatesWithRides(from, to);
		dataAccess.close();
		return dates;
	}

	@Override
	public Ride createRide(String departingCity, String arrivalCity, Date rideDate, int nPlaces, double price,
			String driverEmail) throws RideAlreadyExistException, RideMustBeLaterThanTodayException {
		dataAccess.open();
		Ride r = dataAccess.createRide(departingCity, arrivalCity, rideDate, nPlaces, price, driverEmail);
		dataAccess.close();
		return r;
	}

	@Override
	public User register(String email, String name, String password, boolean isDriver) throws UserAlreadyRegistered {
		dataAccess.open();
		try {
			String hashedPassword = HashUtil.hashPassword(password);
			User u = dataAccess.register(email, name, hashedPassword, isDriver);
			dataAccess.close();
			return u;
		} catch (UserAlreadyRegistered e) {
			dataAccess.close();
			throw e;
		}
	}

	@Override
	public User login(String email, String password) {
		dataAccess.open();
		String hashedPassword = HashUtil.hashPassword(password);
		User u = dataAccess.login(email, hashedPassword);
		dataAccess.close();
		return u;
	}

	@Override
	public Reservation createReservation(Date createDate, long rideId, String travelerEmail, int places)
			throws NotAvailableSeatsException, NotEnoughMoneyException {
		dataAccess.open();
		Reservation r = dataAccess.createReservation(createDate, rideId, travelerEmail, places);
		dataAccess.close();
		return r;
	}

	@Override
	public void depositMoney(String userEmail, double amount) {
		dataAccess.open();
		dataAccess.depositMoney(userEmail, amount);
		dataAccess.close();
	}

	@Override
	public boolean withdrawMoney(String userEmail, double amount) throws NotEnoughMoneyException {
		dataAccess.open();
		boolean result = dataAccess.withdrawMoney(userEmail, amount);
		dataAccess.close();
		return result;
	}

	@Override
	public double getUserBalance(String userEmail) {
		dataAccess.open();
		double balance = dataAccess.getUserBalance(userEmail);
		dataAccess.close();
		return balance;
	}

	@Override
	public List<Transfer> getUserTransfers(String userEmail) {
		dataAccess.open();
		List<Transfer> transferList = dataAccess.getUserTransfers(userEmail);
		dataAccess.close();
		return transferList;
	}

	@Override
	public List<Reservation> getDriverReservations(String userEmail) {
		dataAccess.open();
		List<Reservation> reservations = dataAccess.getDriverReservations(userEmail);
		dataAccess.close();
		return reservations;
	}

	@Override
	public List<Reservation> getTravelerReservations(String travelerEmail) {
		dataAccess.open();
		List<Reservation> reservations = dataAccess.getTravelerReservations(travelerEmail);
		dataAccess.close();
		return reservations;
	}

	@Override
	public Reservation acceptReservation(Long id) {
		dataAccess.open();
		Reservation r = dataAccess.acceptReservation(id);
		dataAccess.close();
		return r;
	}

	@Override
	public Reservation rejectReservation(Long id) {
		dataAccess.open();
		Reservation r = dataAccess.rejectReservation(id);
		dataAccess.close();
		return r;
	}

	@Override
	public User getUser(String email) {
		dataAccess.open();
		User u = dataAccess.getUser(email);
		dataAccess.close();
		return u;
	}

	@Override
	public void deleteUser(String email) {
		dataAccess.open();
		dataAccess.deleteUser(email);
		dataAccess.close();
	}
}
