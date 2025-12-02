package exceptions;

public class RideAlreadyExistException extends Exception {
	public RideAlreadyExistException() {
		super("The ride already exists.");
	}
	public RideAlreadyExistException(String message) {
		super(message);
	}
}
