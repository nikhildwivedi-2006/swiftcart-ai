package in.swiftcart.entity;

import java.util.ArrayList;
import java.util.List;

import in.swiftcart.enums.Role;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false , length = 50)
	private String fullName;
	
	@Column(nullable = false , unique = true , length = 100)
	private String email;
	
	@Column(nullable = false , length = 100)
	private String password;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	@Builder.Default
	private Role role = Role.USER;
	
	@Column(length = 15)
	private String phone;
	
	@Column(length = 250)
	private String address;
	
	@Column(nullable = false)
	@Builder.Default  // for avoiding the default behaviour of builder and protect from the value overriding true to false 
	private Boolean isActive=true;
	
	@OneToOne(mappedBy="user" , cascade=CascadeType.ALL , orphanRemoval=true)
	private Cart cart;
	
	@OneToMany(mappedBy= "user" ,cascade=CascadeType.ALL , orphanRemoval=true)
	@Builder.Default
	private List<Order> orders = new ArrayList<>();  // one to many relationship so we use arraylist one user can do multiple orders
	
}
