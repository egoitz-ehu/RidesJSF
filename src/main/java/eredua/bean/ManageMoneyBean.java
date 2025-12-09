package eredua.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import org.primefaces.component.linechart.LineChart;
import org.primefaces.model.charts.ChartData;
import org.primefaces.model.charts.line.LineChartDataSet;
import org.primefaces.model.charts.line.LineChartModel;

import domain.Transfer;
import domain.User;
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
	private double frozenMoney;

	private double amountToAdd;
	private double amountToWithdraw;
	private List<Transfer> transferList;

	private LineChartModel lineModel;

	@PostConstruct
	public void init() {
		try {
			User user = FacadeBean.getBusinessLogic().getUser(authBean.getUser().getEmail());
			this.currentBalance = user.getMoney();
			this.frozenMoney = user.getFrozenMoney();
			this.transferList = FacadeBean.getBusinessLogic().getUserTransfers(authBean.getUser().getEmail());

			createLineChart();

		} catch (Exception e) {
			this.currentBalance = 0.0;
			this.frozenMoney = 0.0;
		}
	}

	private void createLineChart() {
		ResourceBundle bundle = ResourceBundle.getBundle("messages",
				FacesContext.getCurrentInstance().getViewRoot().getLocale());
		
		lineModel = new LineChartModel();
		ChartData data = new ChartData();

		LineChartDataSet dataSet = new LineChartDataSet();
		List<Double> valuesDouble = this.transferList.stream()
				.map(Transfer::getMoney)
				.toList();
		List<Object> values = valuesDouble.stream()
				.map(d->(Object) d)
				.toList();
		
		System.out.println(values);

		dataSet.setData(values);
		dataSet.setLabel(bundle.getString("money.balanceOverview"));
		
		List<String> labels = this.transferList.stream()
				.map(t -> t.getDate().toString())
	            .toList();

	    data.setLabels(labels);

		dataSet.setBorderColor("rgb(75, 192, 192)");
		dataSet.setTension(0.2);
		dataSet.setFill(true);

		data.addChartDataSet(dataSet);

		lineModel.setData(data);
	}

	public void addMoney() {
		ResourceBundle bundle = ResourceBundle.getBundle("messages",
				FacesContext.getCurrentInstance().getViewRoot().getLocale());
		try {
			FacadeBean.getBusinessLogic().depositMoney(authBean.getUser().getEmail(), amountToAdd);
			String msg = bundle.getString("money.successDeposit");
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "", msg));
			this.currentBalance = FacadeBean.getBusinessLogic().getUserBalance(authBean.getUser().getEmail());
			this.transferList = FacadeBean.getBusinessLogic().getUserTransfers(authBean.getUser().getEmail());
		} catch (Exception e) {
			String msg = bundle.getString("money.errorWithdraw");
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", msg));
		}
	}

	public void withdrawMoney() {
		ResourceBundle bundle = ResourceBundle.getBundle("messages",
				FacesContext.getCurrentInstance().getViewRoot().getLocale());
		try {
			boolean egoera = FacadeBean.getBusinessLogic().withdrawMoney(authBean.getUser().getEmail(),
					amountToWithdraw);
			if (!egoera) {
				String msg = bundle.getString("money.errorWithdraw");
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", msg));
			} else {
				String msg = bundle.getString("money.successWithdraw");
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_INFO, "", msg));
				this.currentBalance = FacadeBean.getBusinessLogic().getUserBalance(authBean.getUser().getEmail());
				this.transferList = FacadeBean.getBusinessLogic().getUserTransfers(authBean.getUser().getEmail());
			}
		} catch (NotEnoughMoneyException e) {
			String msg = bundle.getString("money.notEnoughMoney");
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", msg));
		} catch (Exception e) {
			String msg = bundle.getString("money.errorWithdraw");
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", msg));
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

	public double getFrozenMoney() {
		return frozenMoney;
	}

	public void setFrozenMoney(double frozenMoney) {
		this.frozenMoney = frozenMoney;
	}

	public LineChartModel getLineModel() {
		return lineModel;
	}

	public void setLineModel(LineChartModel lineModel) {
		this.lineModel = lineModel;
	}
}
