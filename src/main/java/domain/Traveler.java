package domain;

import javax.persistence.Entity;

@Entity
public class Traveler extends User {
	public Traveler() {
		super();
	}
	
	public Traveler(String email, String name, String password) {
		super(email, name, password);
	}
}
