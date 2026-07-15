package in.swiftcart.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import in.swiftcart.entity.Order;
import in.swiftcart.enums.OrderStatus;

public interface OrderRepository extends JpaRepository<Order , Long>{
	
	// find orders by user ID
	List<Order> findByUserIdOrderByOrderDateDesc(Long userId);
	
	//find order by user id with pagination 
	Page<Order> findByUserId(Long userId , Pageable pageable);
	
	//find order by status 
	List<Order> findByStatus(OrderStatus status);
	
	//find order with items
	@Query("""
		       SELECT o
		       FROM Order o
		       LEFT JOIN FETCH o.orderItems oi
		       LEFT JOIN FETCH oi.product
		       WHERE o.id = :orderId
		       """)
	Optional<Order> findByIdWithItems(@Param("orderId") Long orderId);
	
	//find order by order number with items 
	@Query("""
		       SELECT o
		       FROM Order o
		       LEFT JOIN FETCH o.orderItems oi
		       LEFT JOIN FETCH oi.product
		       WHERE o.orderNumber = :orderNumber
		       """)
	Optional<Order> findByOrderNumberWithItems(@Param("orderNumber")String orderNumber);
	
	//search orders
	@Query("SELECT o FROM Order o WHERE " +
	       "o.orderNumber LIKE %:keyword% " + //for pattern matching we use %:keyword%"
		   "OR o.user.fullName LIKE %:keyword% " +
	       "OR o.user.email LIKE %:keyword% " )
	Page<Order> searchOrders(@Param("keyword") String keyword , Pageable page);
}
