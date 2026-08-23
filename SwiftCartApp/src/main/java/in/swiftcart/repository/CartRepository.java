package in.swiftcart.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import in.swiftcart.entity.Cart;

public interface CartRepository extends JpaRepository<Cart ,Long>{
	
	// Find cart by user ID
	Optional<Cart> findByUserId(Long userId);

	// Check if user has a cart
	boolean existsByUserId(Long userId);
	
	// Delete cart by user ID
	void deleteByUserId(Long userId);
	
	//find cart by user id with cart items
//	@Query("select c from Cart c " +
//	       "LEFT JOIN FETCH c.cartItems ci " +
//			"LEFT JOIN FETCH ci.product " +
//	       "Where c.user.id= userId"
//			)
	
	//find cart by user id with cart items
	@Query("""
		       SELECT c
		       FROM Cart c
		       LEFT JOIN FETCH c.cartItems ci
		       LEFT JOIN FETCH ci.product
		       WHERE c.user.id = :userId
		       """)
	Optional<Cart> findByUserIdWithItems(@Param("userId")Long userId);
}
