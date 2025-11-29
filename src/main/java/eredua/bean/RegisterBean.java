package eredua.bean;

import java.io.Serializable;

import businessLogic.BLFacade;
import domain.User;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;

@Named("registerBean")
@RequestScoped
public class RegisterBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private String email;
	private String name;
	private String password;
	private String confirmPassword;
	private boolean driver;

	public void register() {
		FacesContext context = FacesContext.getCurrentInstance();
		if(context.isValidationFailed()) return;
		if (!password.equals(confirmPassword)) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Passwords do not match"));
		}
		BLFacade businessLogic = FacadeBean.getBusinessLogic();
		try {
			if (driver) {
				User newDriver = businessLogic.register(email, name, password, true);
				if (newDriver != null) {
					FacesContext.getCurrentInstance().addMessage(null,
							new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Driver registered successfully"));
				}
			} else {
				User newTraveler = businessLogic.register(email, name, password, false);
				if (newTraveler != null) {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
							"Success", "Traveler registered successfully"));
				}
			}

			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Email already exists"));

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Registration failed: " + e.getMessage()));
		}
	}

	// Getters and Setters
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

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	public boolean isDriver() {
		return driver;
	}

	public void setDriver(boolean driver) {
		this.driver = driver;
	}
}
