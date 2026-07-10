package in.swiftcart.dtorequest;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateOrderStatusRequestDTO {

	@NotBlank(message = "order status is required")
	private String orderStatus;
	
	private String notes;
}
