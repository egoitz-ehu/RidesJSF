package eredua.bean;

import java.io.Serializable;
import java.util.List;
import java.util.ResourceBundle;

import domain.Transfer;
import exceptions.NotEnoughMoneyException;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named("manageMoneyBean")
@RequestScoped
public class ManageMoneyBean implements Serializable {
	@Inject
	private AuthBean authBean;
	
	private double currentBalance;
	private double amountToAdd;
	private double amountToWithdraw;
	private List<Transfer> transferList;
	
	@PostConstruct
	public void init() {
		try {
			this.currentBalance = FacadeBean.getBusinessLogic().getUserBalance(authBean.getUser().getEmail());
			this.transferList = FacadeBean.getBusinessLogic().getUserTransfers(authBean.getUser().getEmail());
			System.out.println(transferList);
		} catch (Exception e) {
			this.currentBalance = 0.0;
		}
	}
	
	public void addMoney() {
		ResourceBundle bundle = ResourceBundle.getBundle("messages", FacesContext.getCurrentInstance().getViewRoot().getLocale());
		try {
			FacadeBean.getBusinessLogic().depositMoney(authBean.getUser().getEmail(), amountToAdd);
			String msg = bundle.getString("money.successDeposit");
	        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "", msg));
		} catch (Exception e) {
			String msg = bundle.getString("money.errorWithdraw");
	        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", msg));
		}
	}
	
	public void withdrawMoney() {
		ResourceBundle bundle = ResourceBundle.getBundle("messages", FacesContext.getCurrentInstance().getViewRoot().getLocale());
		try {
			boolean egoera = FacadeBean.getBusinessLogic().withdrawMoney(authBean.getUser().getEmail(), amountToWithdraw);
			if(!egoera) {
		        String msg = bundle.getString("money.errorWithdraw");
		        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", msg));
			} else {
				String msg = bundle.getString("money.successWithdraw");
		        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "", msg));
			}
		} catch (NotEnoughMoneyException e) {
			String msg = bundle.getString("money.notEnoughMoney");
	        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", msg));
		} catch (Exception e) {
			String msg = bundle.getString("money.errorWithdraw");
	        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", msg));
		}
	}

	public double getCurrentBalance() {
		return currentBalance;
	}

	public void setCurrentBalance(double currentBalance) {
		this.currentBalance = currentBalance;
	}

	public double getAmountToAdd() {
		return amountToAdd;
	}

	public void setAmountToAdd(double amountToAdd) {
		this.amountToAdd = amountToAdd;
	}

	public double getAmountToWithdraw() {
		return amountToWithdraw;
	}

	public void setAmountToWithdraw(double amountToWithdraw) {
		this.amountToWithdraw = amountToWithdraw;
	}

	public List<Transfer> getTransferList() {
		return transferList;
	}

	public void setTransferList(List<Transfer> transferList) {
		this.transferList = transferList;
	}
}
