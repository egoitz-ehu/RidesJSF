package eredua.bean;

import java.io.Serializable;
import java.util.ResourceBundle;

import businessLogic.BLFacade;
import domain.User;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;

@Named("registerBean")
@RequestScoped
public class AuthBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private String email;
	private String name;
	private String password;
	private String confirmPassword;
	private boolean wantDriver;

	public void register() {
		FacesContext context = FacesContext.getCurrentInstance();
		ResourceBundle bundle = ResourceBundle.getBundle("messages", FacesContext.getCurrentInstance().getViewRoot().getLocale());
		String msg;
		if(context.isValidationFailed()) return;
		BLFacade businessLogic = FacadeBean.getBusinessLogic();
		try {
			if (wantDriver) {
				User newDriver = businessLogic.register(email, name, password, true);
				if (newDriver != null) {
			        msg = bundle.getString("register.successMessage");
					FacesContext.getCurrentInstance().addMessage(null,
							new FacesMessage(FacesMessage.SEVERITY_INFO, "", msg));
				}
			} else {
				User newTraveler = businessLogic.register(email, name, password, false);
				if (newTraveler != null) {
			        msg = bundle.getString("createRide.errorDate");
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
							"Success", msg));
				}
			}
	        msg = bundle.getString("register.emailExists");
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", msg));

		} catch (Exception e) {
	        msg = bundle.getString("createRide.errorDate");
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", msg));
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
}
