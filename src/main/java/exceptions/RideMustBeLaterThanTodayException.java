package exceptions;

public class RideMustBeLaterThanTodayException extends Exception {
	public RideMustBeLaterThanTodayException() {
		super("The ride date must be later than today.");
	}
	public RideMustBeLaterThanTodayException(String message) {
		super(message);
	}
}
