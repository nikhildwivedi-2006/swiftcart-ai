package in.swiftcart.service;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.razorpay.Utils;

import in.swiftcart.dtorequest.CreatePaymentRequestDTO;
import in.swiftcart.dtorequest.VerifyPaymentRequestDTO;
import in.swiftcart.dtoresponse.CreatePaymentResponseDTO;
import in.swiftcart.entity.Order;
import in.swiftcart.enums.PaymentMethod;
import in.swiftcart.enums.PaymentStatus;
import in.swiftcart.exception.InvalidOperationException;
import in.swiftcart.exception.ResourceNotFoundException;
import in.swiftcart.repository.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class PaymentServiceImpl implements PaymentService {

    private final OrderRepository orderRepository;
    private final RazorpayClient razorpayClient;
    
    @Value("${razorpay.key.id}")
    private String keyId;
    
    @Value("${razorpay.key.secret}")
    private String keySecret;

    @Override
    public CreatePaymentResponseDTO createPayment(CreatePaymentRequestDTO request) {

    	  Order order = orderRepository.findById(request.getOrderId())
                  .orElseThrow(() -> new ResourceNotFoundException("Order", "id", request.getOrderId()));

    	// Check payment method
    	  if (order.getPaymentMethod() != PaymentMethod.RAZORPAY) {
    	      throw new InvalidOperationException("Payment method must be RAZORPAY");
    	  }

    	  // Check payment status
    	  if (order.getPaymentStatus() == PaymentStatus.SUCCESS) {
    	      throw new InvalidOperationException("Payment already completed");
    	  }

    	  try {
    	        JSONObject options = new JSONObject();
    	        options.put("amount", (int) (order.getTotalAmmount() * 100));
    	        options.put("currency", "INR");
    	        options.put("receipt", order.getOrderNumber());

    	        com.razorpay.Order razorpayOrder = razorpayClient.orders.create(options);

    	        order.setRazorpayOrderId(razorpayOrder.get("id").toString());
    	        orderRepository.save(order);

    	        return CreatePaymentResponseDTO.builder()
    	                .razorpayOrderId(razorpayOrder.get("id").toString())
    	                .key(keyId)
    	                .amount(order.getTotalAmmount())
    	                .currency("INR")
    	                .build();

    	    }catch (RazorpayException e) {
    	        e.printStackTrace();
    	        throw new RuntimeException(e.getMessage());
    	    }
          
    }
    
    
    @Override
    public String verifyPayment(VerifyPaymentRequestDTO request) {

        Order order = orderRepository.findById(request.getOrderId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Order", "id", request.getOrderId()));

        try {
            JSONObject options = new JSONObject();
            options.put("razorpay_order_id", request.getRazorpayOrderId());
            options.put("razorpay_payment_id", request.getRazorpayPaymentId());
            options.put("razorpay_signature", request.getRazorpaySignature());

            Utils.verifyPaymentSignature(options, keySecret);

            order.setPaymentStatus(PaymentStatus.SUCCESS);
            order.setRazorpayPaymentId(request.getRazorpayPaymentId());
            order.setRazorpaySignature(request.getRazorpaySignature());

            orderRepository.save(order);

            return "Payment verified successfully";

        } catch (RazorpayException e) {
            order.setPaymentStatus(PaymentStatus.FAILED);
            orderRepository.save(order);

            throw new RuntimeException("Payment verification failed", e);
        }
    }
}