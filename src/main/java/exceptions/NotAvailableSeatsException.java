package exceptions;

public class NotAvailableSeatsException extends Exception{
	public NotAvailableSeatsException(String msg) {
		super(msg);
	}
	
	public NotAvailableSeatsException() {
		super();
	}
}
