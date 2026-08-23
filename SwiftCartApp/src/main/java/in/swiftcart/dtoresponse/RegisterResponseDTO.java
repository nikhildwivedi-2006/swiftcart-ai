package in.swiftcart.dtoresponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterResponseDTO {
	
	 private Long id;
	    private String token;
	    private String fullName;
	    private String email;
	    private String phone;
	    private String address;
    
}