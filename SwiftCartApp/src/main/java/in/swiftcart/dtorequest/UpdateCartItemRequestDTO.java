package in.swiftcart.dtorequest;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
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
public class UpdateCartItemRequestDTO {

	
	@NotNull(message="Quantity must be 1")
	@Min(value= 1 , message="minimum quantity must be 1")
	@Max(value =100 ,message = "Maximum quantity can be 100")
	private Integer quantity;
	
}
