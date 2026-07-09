package in.swiftcart.dtoresponse;

import java.time.LocalDateTime;

import in.swiftcart.dtorequest.AddToCartRequestDTO;
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
public class CartItemResponseDTO {
	
	private Long id;
	private Long productId;
	private String productName;
	private String productImage;
	private String productSku;
	private double unitPrice;
	private Integer quantity;
	private double subTotal;
	private Integer availabelStock;
	private LocalDateTime addedAt;
}
