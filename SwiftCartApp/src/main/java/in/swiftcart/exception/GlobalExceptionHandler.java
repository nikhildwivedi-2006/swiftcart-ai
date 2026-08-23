package in.swiftcart.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

	//Handle ResourceNotFoundException
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException ex , HttpServletRequest request){
		ErrorResponse error = ErrorResponse.builder()
				.timeStamp(LocalDateTime.now())
				.status(HttpStatus.NOT_FOUND.value())
				.error("notfound")
				.message(ex.getMessage())
				.path(request.getRequestURI())
				.build();
		
		return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
	}
	
	
	//Handle DuplicateResourceException
	@ExceptionHandler(DuplicateResourceException.class)
	public ResponseEntity<ErrorResponse> handleDuplicateResourceException(DuplicateResourceException ex , HttpServletRequest request){
		ErrorResponse error = ErrorResponse.builder()
				.timeStamp(LocalDateTime.now())
				.status(HttpStatus.CONFLICT.value())
				.error("conflict")
				.message(ex.getMessage())
				.path(request.getRequestURI())
				.build();
		
		return new ResponseEntity<>(error, HttpStatus.CONFLICT);
	}
	
	//Handle InsufficientStockException
	@ExceptionHandler(InsufficientStockException.class)
	public ResponseEntity<ErrorResponse> handleInsufficientStockException(InsufficientStockException ex , HttpServletRequest request){
		ErrorResponse error = ErrorResponse.builder()
				.timeStamp(LocalDateTime.now())
				.status(HttpStatus.BAD_REQUEST.value())
				.error("Insufficient Stock")
				.message(ex.getMessage())
				.path(request.getRequestURI())
				.build();
		
		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}
	
	//Handle EmptyCartException
	@ExceptionHandler(EmptyCartException.class)
	public ResponseEntity<ErrorResponse> handleEmptyCartException(EmptyCartException ex , HttpServletRequest request){
		ErrorResponse error = ErrorResponse.builder()
				.timeStamp(LocalDateTime.now())
				.status(HttpStatus.BAD_REQUEST.value())
				.error("Empty cart")
				.message(ex.getMessage())
				.path(request.getRequestURI())
				.build();
		
		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}
	
	//Handle InvalidOperationException
	@ExceptionHandler(InvalidOperationException.class)
	public ResponseEntity<ErrorResponse> handleInvalidOperationException(InvalidOperationException ex , HttpServletRequest request){
		ErrorResponse error = ErrorResponse.builder()
				.timeStamp(LocalDateTime.now())
				.status(HttpStatus.BAD_REQUEST.value())
				.error("Invalid Operation")
				.message(ex.getMessage())
				.path(request.getRequestURI())
				.build();
		
		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}
	
	//Handle Validation Exceptions
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex , HttpServletRequest request){
		
		Map<String , String> validationErrors= new HashMap<>();
		for(FieldError error:ex.getBindingResult().getFieldErrors()) {
			validationErrors.put(error.getField(),error.getDefaultMessage());
		}
		
		ErrorResponse error = ErrorResponse.builder()
				.timeStamp(LocalDateTime.now())
				.status(HttpStatus.BAD_REQUEST.value())
				.error("Validation Error")
				.message("Input Validation Failed. Please check the error")
				.validationErrors(validationErrors)
				.path(request.getRequestURI())
				.build();
		
		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}
	
	//Handle Method Argument Type Mismatch Exception
	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity<ErrorResponse> handleTypeMismatchException(MethodArgumentTypeMismatchException ex , HttpServletRequest request){
		
		Class c = ex.getRequiredType(); // for avoiding null pointer exception in method getRequiredType
		String Message = String.format("Invalid value '%s' for the parameter '%s' . Expected type : %s' ", ex.getValue(), ex.getName() , c!=null?c.getSimpleName():"unknown");
		
		ErrorResponse error = ErrorResponse.builder()
				.timeStamp(LocalDateTime.now())
				.status(HttpStatus.BAD_REQUEST.value())
				.error(" Type Mismatch")
				.message("Input Validation Failed. Please check the error")
				.path(request.getRequestURI())
				.build();
		
		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}
	
	//Handle Illegal Argument Exception
	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex , HttpServletRequest request){
		
		
		ErrorResponse error = ErrorResponse.builder()
				.timeStamp(LocalDateTime.now())
				.status(HttpStatus.BAD_REQUEST.value())
				.error(" Wrong Argument")
				.message(ex.getMessage())
				.path(request.getRequestURI())
				.build();
		
		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}
	
	
	// Handle Invalid Token Exception (JWT related)
	@ExceptionHandler(InvalidTokenException.class)
	public ResponseEntity<ErrorResponse> handleInvalidTokenException(InvalidTokenException ex, HttpServletRequest request) {

	    ErrorResponse error = ErrorResponse.builder()
	            .timeStamp(LocalDateTime.now())
	            .status(HttpStatus.UNAUTHORIZED.value())
	            .error("Invalid Token")
	            .message(ex.getMessage())
	            .path(request.getRequestURI())
	            .build();

	    return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
	}
	
	// Handle all other remaining Exception 
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleException(Exception ex , HttpServletRequest request){
		
		
		ErrorResponse error = ErrorResponse.builder()
				.timeStamp(LocalDateTime.now())
				.status(HttpStatus.INTERNAL_SERVER_ERROR.value())
				.error("Internal Server Error")
				.message(ex.getMessage())
				.path(request.getRequestURI())
				.build();
		
		return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	//Authentication exception
	@ExceptionHandler(AuthenticationException.class)
	public ResponseEntity<ErrorResponse> handleAuthenticationException(
	        AuthenticationException ex,
	        HttpServletRequest request) {

	    ErrorResponse error = ErrorResponse.builder()
	            .timeStamp(LocalDateTime.now())
	            .status(HttpStatus.UNAUTHORIZED.value())
	            .error("Authentication Failed")
	            .message(ex.getMessage())
	            .path(request.getRequestURI())
	            .build();

	    return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
	}
	
	// payment Exception
	@ExceptionHandler(PaymentException.class)
	public ResponseEntity<ErrorResponse> handlePaymentException(
	        PaymentException ex,
	        HttpServletRequest request) {

	    ErrorResponse error = ErrorResponse.builder()
	            .timeStamp(LocalDateTime.now())
	            .status(HttpStatus.BAD_REQUEST.value())
	            .error("Payment Error")
	            .message(ex.getMessage())
	            .path(request.getRequestURI())
	            .build();

	    return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}
	
}
