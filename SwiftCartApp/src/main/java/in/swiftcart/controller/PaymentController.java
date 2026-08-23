package in.swiftcart.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import in.swiftcart.dtorequest.CreatePaymentRequestDTO;
import in.swiftcart.dtorequest.VerifyPaymentRequestDTO;
import in.swiftcart.dtoresponse.ApiResponseDTO;
import in.swiftcart.dtoresponse.CreatePaymentResponseDTO;
import in.swiftcart.service.PaymentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
@Tag(name = "Payments", description = "APIs for payment processing")
public class PaymentController {

	private final PaymentService paymentService;

	@PostMapping("/create-order")
	public ResponseEntity<ApiResponseDTO<CreatePaymentResponseDTO>> createPayment(
			@Valid @RequestBody CreatePaymentRequestDTO request) {

		CreatePaymentResponseDTO response = paymentService.createPayment(request);

		return ResponseEntity.ok(ApiResponseDTO.success("Payment order created successfully", response));
	}

	@PostMapping("/verify")
	public ResponseEntity<ApiResponseDTO<String>> verifyPayment(@Valid @RequestBody VerifyPaymentRequestDTO request) {

		String response = paymentService.verifyPayment(request);

		return ResponseEntity.ok(ApiResponseDTO.success("Payment verified successfully", response));
	}
}