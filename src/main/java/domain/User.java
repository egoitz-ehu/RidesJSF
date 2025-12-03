package domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class User {
	@Id
	private String email;
	private String name;
	private String password;
	
	private double money;
	private double frozenMoney;
	
	public User() {
		
	}
	
	public User(String email, String name, String password) {
		this.email=email;
		this.name=name;
		this.password=password;
		this.money=0;
		this.frozenMoney=0;
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
}
