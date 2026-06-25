package in.swiftcart.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import in.swiftcart.entity.Product;

public interface ProductRepository extends JpaRepository<Product , Long>{
	
	//find product by sku(stock keeping units)
	Optional<Product> findBySku(String sku);
	
	//check if sku exists
	boolean existsBySku(String sku);
	
	//find all avilable products
	List<Product>findByIsAvilableTrue();
	
	//find products by category
	List<Product>findByCategoryIgnoreCase(String category);
	
	// find products within price range
	List<Product>findByPriceBetween(double minPrice , double maxPrice);
	
	//search product by name or description
	 @Query("SELECT p FROM Product p WHERE " +
	            "(LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
	            "OR LOWER(p.description) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
	            "AND p.isAvailable = true")
	public List<Product> searchProducts(@Param("keyword")String keyword);
	 
	 
	// Update stock quantity
	    @Modifying
	    @Query("UPDATE Product p SET p.stockQuantity = p.stockQuantity - :quantity WHERE p.id = :productId AND p.stockQuantity >= :quantity")
	    int decreaseStock(@Param("productId") Long productId, @Param("quantity") Integer quantity);

	    // Increase stock quantity
	    @Modifying
	    @Query("UPDATE Product p SET p.stockQuantity = p.stockQuantity + :quantity WHERE p.id = :productId")
	    int increaseStock(@Param("productId") Long productId, @Param("quantity") Integer quantity);

	    // Find low stock products
	    @Query("SELECT p FROM Product p WHERE p.stockQuantity <= :threshold AND p.isAvailable = true")
	    List<Product> findLowStockProducts(@Param("threshold") Integer threshold);
	

}
