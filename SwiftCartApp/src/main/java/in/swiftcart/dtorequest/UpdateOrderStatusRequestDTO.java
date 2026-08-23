package in.swiftcart.dtorequest;

import in.swiftcart.enums.OrderStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateOrderStatusRequestDTO {

	@NotNull(message = "Order status is required")
	private OrderStatus orderStatus;
	
	private String notes;
}
