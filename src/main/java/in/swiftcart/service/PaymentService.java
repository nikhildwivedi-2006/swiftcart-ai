package in.swiftcart.service;

import in.swiftcart.dtorequest.CreatePaymentRequestDTO;
import in.swiftcart.dtorequest.VerifyPaymentRequestDTO;
import in.swiftcart.dtoresponse.CreatePaymentResponseDTO;

public interface PaymentService {

    CreatePaymentResponseDTO createPayment(CreatePaymentRequestDTO request);

    String verifyPayment(VerifyPaymentRequestDTO request);
}