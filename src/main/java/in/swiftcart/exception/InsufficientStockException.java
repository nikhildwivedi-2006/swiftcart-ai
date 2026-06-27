package in.swiftcart.exception;

public class InsufficientStockException extends RuntimeException{

	// when stock quantity is less and we want to order more than stock than it appears
	public InsufficientStockException(String message) {
		super(message);
	}
	
	
}
