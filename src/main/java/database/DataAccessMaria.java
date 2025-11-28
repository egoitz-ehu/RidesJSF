package database;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import domain.Ride;
import eredua.JPAUtil;

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
		TypedQuery<String> query = db.createQuery("SELECT DISTINCT r.arrivalCity FROM Ride r WHERE r.from=:from ORDER BY r.to", String.class);
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
}
