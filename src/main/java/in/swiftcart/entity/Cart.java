package in.swiftcart.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
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
public class Cart {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="user_id" , nullable =false , unique=true)
	private User user;
	
	@OneToMany(mappedBy="cart" , cascade=CascadeType.ALL , orphanRemoval= true)
	@Builder.Default
	private List<CartItems> cartItems = new ArrayList<>();
	
	@Column
	@Builder.Default
	private double totalAmmount = 0.0;
	
	@Column(nullable=false)
	@Builder.Default
	private Integer totalItems=0;

	@CreationTimestamp
	@Column(updatable=false)
	private LocalDateTime createdAt;
	
	@UpdateTimestamp
	private LocalDateTime updatedAt;
	
}
