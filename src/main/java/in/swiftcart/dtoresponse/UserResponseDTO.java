package in.swiftcart.dtoresponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponseDTO {

	private Long id;
	private String fullName;
	private String email;
	private String phone;
	private String address;
	private boolean isActive;
	
}
