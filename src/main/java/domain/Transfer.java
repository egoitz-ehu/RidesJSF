package domain;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Transfer {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private double amount;
	
	private double money;
	private double frozen;
	
	@ManyToOne(fetch=FetchType.LAZY)
	private User user;
	
	@Enumerated(EnumType.STRING)
	private TransferType type;
	
	//Transferentzia bidaia baten inguruan denean
	@ManyToOne(fetch=FetchType.LAZY)
	private Ride ride;
	@ManyToOne(fetch=FetchType.LAZY)
	private Reservation reservation;
	
	public Transfer() {
		
	}
	
	public Transfer(double amount, User user, TransferType type, double oldMoney, double oldFrozen) {
		this.amount=amount;
		this.user=user;
		this.type=type;
		this.ride=null;
		this.money=oldMoney;
		this.frozen=oldFrozen;
	}
	
	public Transfer(double amount, User user, TransferType type, Reservation reservation, double oldMoney, double oldFrozen) {
		this.amount=amount;
		this.user=user;
		this.type=type;
		this.ride=reservation.getRide();
		this.reservation=reservation;
		this.money=oldMoney;
		this.frozen=oldFrozen;
	}
	
	public Transfer(double amount, User user, TransferType type, Ride ride, Reservation reservation, double oldMoney, double oldFrozen) {
		this.amount=amount;
		this.user=user;
		this.type=type;
		this.ride=ride;
		this.reservation=reservation;
		this.money=oldMoney;
		this.frozen=oldFrozen;
	}


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public TransferType getType() {
		return type;
	}

	public void setType(TransferType type) {
		this.type = type;
	}

	public Ride getRide() {
		return ride;
	}

	public void setRide(Ride ride) {
		this.ride = ride;
	}

	public Reservation getReservation() {
		return reservation;
	}

	public void setReservation(Reservation reservation) {
		this.reservation = reservation;
	}

	public double getOldMoney() {
		return money;
	}

	public void setOldMoney(double oldMoney) {
		this.money = oldMoney;
	}

	public double getOldFrozen() {
		return frozen;
	}

	public void setOldFrozen(double oldFrozen) {
		this.frozen = oldFrozen;
	}
	
	
}
