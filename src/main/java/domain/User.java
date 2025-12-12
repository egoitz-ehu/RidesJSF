package domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class User {
	@Id
	private String email;
	private String name;
	private String password;
	
	private double money;
	private double frozenMoney;
	
	@OneToMany(fetch=FetchType.LAZY, targetEntity = Transfer.class, mappedBy = "user", cascade= {CascadeType.PERSIST, CascadeType.REMOVE })
	private List<Transfer> transferList;
	
	public User() {
		
	}
	
	public User(String email, String name, String password) {
		this.email=email;
		this.name=name;
		this.password=password;
		this.money=0;
		this.frozenMoney=0;
		this.transferList = new ArrayList<Transfer>();
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public double getMoney() {
		return money;
	}

	public void setMoney(double money) {
		this.money = money;
	}

	public double getFrozenMoney() {
		return frozenMoney;
	}

	public void setFrozenMoney(double frozenMoney) {
		this.frozenMoney = frozenMoney;
	}
	
	public List<Transfer> getTransferList() {
		return transferList;
	}

	public void setTransferList(List<Transfer> transferList) {
		this.transferList = transferList;
	}

	public void moveMoneyToFrozen(double amount) {
		if(amount>0 && amount<=this.money) {
			this.money -= amount;
			this.frozenMoney += amount;
		}
	}
	
	public void addTransfer(Transfer t) {
		this.transferList.add(t);
	};
	
	public Transfer createTransfer(double amount, TransferType type, double money, double frozen) {
		if(amount>0 && type!=null) {
			Transfer transfer = new Transfer(amount, this, type, money, frozen);
			this.addTransfer(transfer);
			return transfer;
		} else {
			return null;
		}
	}
	
	public void removeFrozenMoney(double amount) {
		if(amount<=this.frozenMoney && amount>0) {
			this.frozenMoney-=amount;
		}
	}
	
	public void addFrozenMoney(double amount) {
		if(amount>0) {
			this.frozenMoney+=amount;
		}
	}
	
	public void moveFrozenToMoney(double amount) {
		if(amount>0 && frozenMoney>=amount) {
			this.money+=amount;
			this.frozenMoney-=amount;
		}
	}
}
