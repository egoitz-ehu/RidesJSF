package database;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import domain.Driver;
import domain.Ride;
import domain.Traveler;
import domain.User;
import eredua.JPAUtil;
import exceptions.RideAlreadyExistException;
import exceptions.RideMustBeLaterThanTodayException;
import exceptions.UserAlreadyRegistered;
import util.UtilDate;

public class DataAccessMaria {
	private EntityManager db;

	public void open() {
		db = JPAUtil.getEntityManager();
	}

	public void close() {
		if (db != null && db.isOpen()) {
			db.close();
		}
	}

	public List<String> getDepartCities() {
		TypedQuery<String> query = db.createQuery("SELECT DISTINCT r.departingCity FROM Ride r", String.class);
		return query.getResultList();
	}

	public List<String> getArrivalCities(String departingCity) {
		TypedQuery<String> query = db.createQuery(
				"SELECT DISTINCT r.arrivalCity FROM Ride r WHERE r.departingCity=:from ORDER BY r.arrivalCity",
				String.class);
		query.setParameter("from", departingCity);
		return query.getResultList();
	}

	public List<Ride> getRidesByValues(String departingCity, String arrivalCity, Date rideDate) {
		if (departingCity == null || arrivalCity == null || rideDate == null)
			return new ArrayList<Ride>();
		try {
			return db.createQuery(
					"SELECT r FROM Ride r WHERE r.departingCity=:departingCity AND r.arrivalCity=:arrivalCity AND r.rideDate=:rideDate",
					Ride.class).setParameter("departingCity", departingCity).setParameter("arrivalCity", arrivalCity)
					.setParameter("rideDate", rideDate).getResultList();
		} catch (Exception e) {
			System.out.println(e);
			return new ArrayList<Ride>();
		}
	}

	public List<Date> getThisMonthDatesWithRides(String from, String to, Date date) {
		Date firstDayMonthDate = UtilDate.firstDayMonth(date);
		Date lastDayMonthDate = UtilDate.lastDayMonth(date);

		TypedQuery<Date> query = db.createQuery(
				"SELECT DISTINCT r.rideDate FROM Ride r WHERE r.departingCity=?1 AND r.arrivalCity=?2 AND r.rideDate BETWEEN ?3 and ?4",
				Date.class);

		query.setParameter(1, from);
		query.setParameter(2, to);
		query.setParameter(3, firstDayMonthDate);
		query.setParameter(4, lastDayMonthDate);
		return query.getResultList();
	}

	public Ride createRide(String from, String to, Date date, int nPlaces, float price, String driverEmail)
			throws RideAlreadyExistException, RideMustBeLaterThanTodayException {
		if (from == null || to == null || date == null || nPlaces <= 0 || price < 0 || driverEmail == null)
			return null;
		if (new Date().compareTo(date) > 0) {
			throw new RideMustBeLaterThanTodayException("Ride date must be later than today");
		}
		db.getTransaction().begin();
		Driver driver = db.find(Driver.class, driverEmail);
		if (driver == null) {
			System.out.println("Driver ez da aurkitu");
			db.getTransaction().rollback();
			return null;
		}
		if (driver.doesRideExists(from, to, date)) {
			db.getTransaction().rollback();
			throw new RideAlreadyExistException("Driver already has a equal ride");
		}
		Ride ride = driver.addRide(from, to, date, nPlaces, price);
		db.persist(driver);
		db.getTransaction().commit();
		return ride;
	}

	public User register(String email, String name, String password, boolean isDriver) throws UserAlreadyRegistered {
		if (email == null || name == null || password == null)
			return null;
		try {
			db.getTransaction().begin();
			User u = db.find(User.class, email);
			if (u != null) {
				db.getTransaction().rollback();
				throw new UserAlreadyRegistered("Already exists a user with the same email");
			}
			User newUser;
			if (isDriver) {
				newUser = new Driver(email, name, password);
			} else {
				newUser = new Traveler(email, name, password);
			}
			db.persist(newUser);
			db.getTransaction().commit();
			return newUser;
		} catch (Exception e) {
			if (db.getTransaction().isActive()) {
				db.getTransaction().rollback();
			}
			throw e;
		}
	}
	
	public User login(String email, String password) {
		if(email==null || password==null) return null;
		TypedQuery<User> query = db.createQuery("SELECT u FROM User u WHERE u.email=?email AND u.password=?password", User.class);
		query.setParameter("email", email);
		query.setParameter("password", password);
		User u = query.getSingleResult();
		return u;
	}
}
