package eredua.bean;

import java.io.Serializable;
import java.util.ResourceBundle;

import businessLogic.BLFacade;
import domain.Driver;
import domain.Traveler;
import domain.User;
import exceptions.UserAlreadyRegistered;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;

@Named("authBean")
@SessionScoped
public class AuthBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private String email;
	private String name;
	private String password;
	private String confirmPassword;
	private boolean wantDriver;

	private User user;

	public String register() {
		FacesContext context = FacesContext.getCurrentInstance();
		ResourceBundle bundle = ResourceBundle.getBundle("messages",
				FacesContext.getCurrentInstance().getViewRoot().getLocale());
		String msg;
		if (context.isValidationFailed())
			return null;
		BLFacade businessLogic = FacadeBean.getBusinessLogic();
		try {
			if (wantDriver) {
				User newDriver = businessLogic.register(email, name, password, true);
				if (newDriver != null) {
					msg = bundle.getString("register.successMessage");
					FacesContext.getCurrentInstance().addMessage(null,
							new FacesMessage(FacesMessage.SEVERITY_INFO, "", msg));
				}
				user = newDriver;
			} else {
				User newTraveler = businessLogic.register(email, name, password, false);
				if (newTraveler != null) {
					msg = bundle.getString("register.successMessage");
					FacesContext.getCurrentInstance().addMessage(null,
							new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", msg));
				}
				user = newTraveler;
			}
			return "sarrera";
		} catch (UserAlreadyRegistered e) {
			msg = bundle.getString("register.emailExists");
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", msg));
			return null;

		} catch (Exception e) {
			msg = bundle.getString("register.failed");
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", msg));
			return null;
		}
	}

	public String login() {
		BLFacade businessLogic = FacadeBean.getBusinessLogic();
		User u = businessLogic.login(email, password);
		ResourceBundle bundle = ResourceBundle.getBundle("messages",
				FacesContext.getCurrentInstance().getViewRoot().getLocale());
		if (u == null) {
			String msg = bundle.getString("login.error");
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", msg));
			return null;
		} else {
			String msg = bundle.getString("login.success");
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "", msg));
			this.user = u;
			return "sarrera";
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

	public boolean isWantDriver() {
		return wantDriver;
	}

	public void setWantDriver(boolean driver) {
		this.wantDriver = driver;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public boolean isDriver() {
		if (user == null) {
			System.out.println("User is null");
			return false;
		}
		boolean result = user instanceof Driver;
		System.out.println("User class: " + user.getClass().getName() + " - isDriver: " + result);
		return result;
	}

	public boolean isTraveler() {
		if (user == null) {
			System.out.println("User is null");
			return false;
		}
		boolean result = user instanceof Traveler;
		System.out.println("User class: " + user.getClass().getName() + " - isTraveler: " + result);
		return result;
	}
}
