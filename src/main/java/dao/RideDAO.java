package dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;

import domain.Ride;

public class RideDAO {
	private EntityManager entityManager;

	public List<Ride> getRidesByValues(String departingCity, String arrivalCity, Date rideDate) {
		if (departingCity == null || arrivalCity == null || rideDate == null)
			return new ArrayList<Ride>();
		try {
			return entityManager.createQuery(
					"SELECT r FROM Ride r WHERE r.departingCity=:departingCity AND r.arrivalCity=:arrivalCity AND r.rideDate=:rideDate",
					Ride.class).setParameter("departingCity", departingCity).setParameter("arrivalCity", arrivalCity)
					.setParameter("rideDate", rideDate).getResultList();
		} catch (Exception e) {
			System.out.println(e);
			return new ArrayList<Ride>();
		}
	}
}
