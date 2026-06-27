package in.swiftcart.exception;

import java.time.LocalDateTime;
import java.util.Map;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
public class ErrorResponse {
	
	private LocalDateTime timeStamp;
	
	private int status;
	
	private String message;
	
	private String error;
	
	private String path;
	
	private Map<String , String>validationErrors;

	public ErrorResponse(LocalDateTime timeStamp, int status, String message, String error, String path,
			Map<String, String> validationErrors) {
		
		this.timeStamp = timeStamp;
		this.status = status;
		this.message = message;
		this.error = error;
		this.path = path;
		this.validationErrors = validationErrors;
	}
	
	
	
}
