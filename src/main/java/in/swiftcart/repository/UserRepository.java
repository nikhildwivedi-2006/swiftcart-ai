package in.swiftcart.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import in.swiftcart.entity.User;

public interface UserRepository extends JpaRepository<User, Long>{ //entity class name and primary key data type in angular bracket
	
	public Optional<User> findByEmail(String email);
	
	public boolean existsByEmail(String email);
	
	public List<User> findByIsActiveTrue();	
	
	@Query("SELECT u FROM User u WHERE LOWER(u.fullName) like LOWER(CONCAT('%' , :keyword , '%')) " + 
	       "or LOWER(u.email) like LOWER(CONCAT('%' , :keyword , '%')) ")
	public List<User> searchByNameOrEmail(@Param("keyword")String keyword);
}
