package dataAccess;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import domain.Driver;
import domain.Reservation;
import domain.ReservationState;
import domain.Ride;
import domain.Transfer;
import domain.TransferType;
import domain.Traveler;
import domain.User;
import eredua.JPAUtil;
import exceptions.NotAvailableSeatsException;
import exceptions.NotEnoughMoneyException;
import exceptions.RideAlreadyExistException;
import exceptions.RideMustBeLaterThanTodayException;
import exceptions.UserAlreadyRegistered;
import util.UtilDate;

public class HibernateDataAccess {
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
		System.out.println(date);

		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH);

		System.out.println("YEAR: " + year + ", MONTH: " + month);

		String jpql = "SELECT DISTINCT r.rideDate FROM Ride r " + "WHERE r.departingCity = :fromCity "
				+ "AND r.arrivalCity = :toCity " + "AND YEAR(r.rideDate) = :year " + "AND MONTH(r.rideDate) = :month";

		TypedQuery<Date> query = db.createQuery(jpql, Date.class);
		query.setParameter("fromCity", from);
		query.setParameter("toCity", to);
		query.setParameter("year", year);
		query.setParameter("month", month + 1);

		List<Date> results = query.getResultList();
		System.out.println("FOUND: " + results.size() + " dates");
		System.out.println(results);
		return results;
	}

	public List<Date> getThisMonthDatesWithRides(String from, String to) {
		String jpql = "SELECT DISTINCT r.rideDate FROM Ride r " + "WHERE r.departingCity = :fromCity "
				+ "AND r.arrivalCity = :toCity " + "AND r.rideDate >= CURDATE()";

		TypedQuery<Date> query = db.createQuery(jpql, Date.class);
		query.setParameter("fromCity", from);
		query.setParameter("toCity", to);

		List<Date> results = query.getResultList();
		return results;
	}

	public Ride createRide(String from, String to, Date date, int nPlaces, double price, String driverEmail)
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
		//lazy denez kargatu
		driver.getRides().size();
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
		try {
			if (email == null || password == null)
				return null;

			TypedQuery<Driver> driverQuery = db
					.createQuery("SELECT d FROM Driver d WHERE d.email=:email AND d.password=:password", Driver.class);
			driverQuery.setParameter("email", email);
			driverQuery.setParameter("password", password);

			try {
				Driver driver = driverQuery.getSingleResult();
				return driver;
			} catch (NoResultException e) {
				TypedQuery<Traveler> travelerQuery = db.createQuery(
						"SELECT t FROM Traveler t WHERE t.email=:email AND t.password=:password", Traveler.class);
				travelerQuery.setParameter("email", email);
				travelerQuery.setParameter("password", password);

				try {
					Traveler traveler = travelerQuery.getSingleResult();
					return traveler;
				} catch (NoResultException e2) {
					return null;
				}
			}
		} catch (Exception e) {
			return null;
		}
	}

	public Reservation createReservation(Date date, long rideId, String travelerEmail, int places)
			throws NotAvailableSeatsException, NotEnoughMoneyException {
		if (date == null || travelerEmail == null || places <= 0)
			return null;
		try {
			db.getTransaction().begin();
			Ride r = db.find(Ride.class, rideId);
			if (r == null) {
				db.getTransaction().rollback();
				return null;
			}
			if (r.getAvailableSeats() < places) {
				db.getTransaction().rollback();
				throw new NotAvailableSeatsException();
			}
			Traveler t = db.find(Traveler.class, travelerEmail);
			if (t == null) {
				db.getTransaction().rollback();
				return null;
			}
			double totalPrice = places * r.getPricePerSeat();
			double oldAmount = t.getMoney();
			if (totalPrice > oldAmount) {
				db.getTransaction().rollback();
				throw new NotEnoughMoneyException();
			}
			Reservation reservation = r.createReservation(date, t, places);
			t.addReservation(reservation);
			t.moveMoneyToFrozen(totalPrice);
			t.createTransfer(totalPrice, TransferType.RESERVATION_REQUEST, t.getMoney(), t.getFrozenMoney());
			db.persist(r);
			db.getTransaction().commit();
			return reservation;
		} catch (Exception e) {
			db.getTransaction().rollback();
			return null;
		}
	}

	public void depositMoney(String userEmail, double amount) {
		if (userEmail == null || amount <= 0)
			return;
		try {
			db.getTransaction().begin();
			User u = db.find(User.class, userEmail);
			if (u == null) {
				db.getTransaction().rollback();
				return;
			}
			double oldAmount = u.getMoney();
			u.setMoney(oldAmount + amount);
			u.createTransfer(amount, TransferType.DEPOSIT, u.getMoney(), u.getFrozenMoney());
			db.persist(u);
			db.getTransaction().commit();
		} catch (Exception e) {
			db.getTransaction().rollback();
		}
	}

	public boolean withdrawMoney(String userEmail, double amount) throws NotEnoughMoneyException {
		if (userEmail == null || amount <= 0)
			return false;
		try {
			db.getTransaction().begin();
			User u = db.find(User.class, userEmail);
			if (u == null || u.getMoney() < amount) {
				db.getTransaction().rollback();
				throw new NotEnoughMoneyException();
			}
			double oldAmount = u.getMoney();
			u.setMoney(oldAmount - amount);
			u.createTransfer(amount, TransferType.WITHDRAWAL, u.getMoney(), u.getFrozenMoney());
			db.persist(u);
			db.getTransaction().commit();
			return true;
		} catch (NotEnoughMoneyException e) {
			throw e;
		} catch (Exception e) {
			db.getTransaction().rollback();
			return false;
		}
	}

	public double getUserBalance(String userEmail) {
		if (userEmail == null)
			return 0.0;
		try {
			User u = db.find(User.class, userEmail);
			if (u == null)
				return 0.0;
			return u.getMoney();
		} catch (Exception e) {
			return 0.0;
		}
	}

	public List<Transfer> getUserTransfers(String userEmail) {
		if (userEmail == null)
			return new ArrayList<Transfer>();
		try {
			db.getTransaction().begin();
			User u = db.find(User.class, userEmail);
			if (u == null)
				return new ArrayList<Transfer>();
			// Lista kargatu, lazy estrategia erabiltzen delako
			u.getTransferList().size();
			List<Transfer> transferList = u.getTransferList();
			return transferList;
		} catch (Exception e) {
			return new ArrayList<Transfer>();
		}
	}

	public List<Reservation> getDriverReservations(String driverEmail) {
		if (driverEmail == null)
			return new ArrayList<Reservation>();
		try {
			TypedQuery<Reservation> q = db.createQuery(
					"SELECT res FROM Reservation res WHERE res.ride.driver.email = :email", Reservation.class);
			q.setParameter("email", driverEmail);
			List<Reservation> reservations = q.getResultList();
			return reservations;
		} catch (Exception e) {
			return new ArrayList<Reservation>();
		}
	}
	
	public List<Reservation> getTravelerReservations(String travelerEmail) {
		if (travelerEmail == null)
			return new ArrayList<Reservation>();
		try {
			TypedQuery<Reservation> q = db.createQuery("SELECT res FROM Reservation res WHERE res.traveler.email=:email", Reservation.class);
			q.setParameter("email", travelerEmail);
			List<Reservation> reservations = q.getResultList();
			return reservations;
		} catch(Exception e) {
			return new ArrayList<Reservation>();
		}
	}
	
	public Reservation acceptReservation(Long reservationId) {
		try {
			db.getTransaction().begin();
			Reservation reservation = db.find(Reservation.class, reservationId);
			if(!reservation.getState().equals(ReservationState.WAITING)) {
				db.getTransaction().rollback();
			}
			reservation.setState(ReservationState.ACCEPTED);
			double amount = reservation.getTotalPrice();
			Traveler t = reservation.getTraveler();
			t.removeFrozenMoney(amount);
			t.createTransfer(amount, TransferType.RESERVATION_ACCEPT_TRAVELER, t.getMoney(), t.getFrozenMoney());
			Driver d = reservation.getRide().getDriver();
			d.addFrozenMoney(amount);
			d.createTransfer(amount, TransferType.RESERVATION_ACCEPT_DRIVER, d.getMoney(), d.getFrozenMoney());
			db.persist(d);
			db.getTransaction().commit();
			return reservation;
		} catch(Exception e) {
			db.getTransaction().rollback();
			return null;
		}
	}
	
	public Reservation rejectReservation(Long reservationId) {
		try {
			db.getTransaction().begin();
			Reservation reservation = db.find(Reservation.class, reservationId);
			if(!reservation.getState().equals(ReservationState.WAITING)) {
				db.getTransaction().rollback();
			}
			reservation.setState(ReservationState.REJECTED);
			double amount = reservation.getTotalPrice();
			Ride r = reservation.getRide();
			r.setAvailableSeats(r.getAvailableSeats()+reservation.getnPlaces());
			Traveler t = reservation.getTraveler();
			t.moveFrozenToMoney(amount);
			t.createTransfer(amount, TransferType.RESERVATION_REJECT, t.getMoney(), t.getFrozenMoney());
			db.persist(r);
			db.getTransaction().commit();
			return reservation;
		} catch(Exception e) {
			db.getTransaction().rollback();
			return null;
		}
	}
	
	public User getUser(String userEmail) {
		if (userEmail == null)
			return null;
		try {
			User u = db.find(User.class, userEmail);
			return u;
		} catch (Exception e) {
			return null;
		}
	}
}
