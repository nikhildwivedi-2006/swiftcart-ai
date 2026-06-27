package in.swiftcart.exception;

public class InvalidOperationException extends RuntimeException{

	// User tries to return an order after the return period has expired
	
	public InvalidOperationException(String message) {
		super(message);
	}

	
	
	
}
