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
public class CreatePaymentResponseDTO {

    private String razorpayOrderId;

    private String key;

    private Double amount;

    private String currency;
}