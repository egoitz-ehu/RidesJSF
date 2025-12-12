package eredua.bean;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import domain.Ride;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;

@Named("dena")
@SessionScoped
public class BidaiaDenakDataBean implements Serializable {
	private Date rideDate;
	
	private List<Ride> rideList;
	private int kopurua;

	public Date getRideDate() {
		return rideDate;
	}

	public void setRideDate(Date rideDate) {
		this.rideDate = rideDate;
	}
	
	public String bidaiakLortu() {
		rideList = FacadeBean.getBusinessLogic().getDateAllRides(this.rideDate);
		this.kopurua = rideList.size();
		return "bidaiakIkusi";
	}

	public List<Ride> getRideList() {
		return rideList;
	}

	public void setRideList(List<Ride> rideList) {
		this.rideList = rideList;
	}

	public int getKopurua() {
		return kopurua;
	}

	public void setKopurua(int kopurua) {
		this.kopurua = kopurua;
	}
}
