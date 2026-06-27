package in.swiftcart.exception;

public class EmptyCartException extends RuntimeException{

	// this exception appears when cart is empty and we want to place order 
	public EmptyCartException(String message) {
		super(message);
	}
	
	
}
