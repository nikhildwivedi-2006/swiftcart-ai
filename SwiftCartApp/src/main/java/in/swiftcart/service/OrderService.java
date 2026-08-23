package in.swiftcart.service;

import java.util.List;

import in.swiftcart.dtorequest.PlaceOrderRequestDTO;
import in.swiftcart.dtorequest.UpdateOrderStatusRequestDTO;
import in.swiftcart.dtoresponse.OrderResponseDTO;
import in.swiftcart.dtoresponse.PageResponseDTO;
import in.swiftcart.enums.OrderStatus;

public interface OrderService {

    OrderResponseDTO placeOrder(PlaceOrderRequestDTO placeOrderRequestDTO);

    OrderResponseDTO getOrderById(Long orderId);

    OrderResponseDTO getOrderByOrderNumber(String orderNumber);

    List<OrderResponseDTO> getOrdersByUserId(Long userId);

    PageResponseDTO<OrderResponseDTO> getAllOrdersPaginated(int page, int size, String sortBy, String sortDir);

    List<OrderResponseDTO> getOrdersByStatus(OrderStatus status);

    OrderResponseDTO updateOrderStatus(Long orderId, UpdateOrderStatusRequestDTO updateOrderStatusRequestDTO);

    OrderResponseDTO cancelOrder(Long orderId, String reason);

    PageResponseDTO<OrderResponseDTO> searchOrders(String keyword, int page, int size);
}
