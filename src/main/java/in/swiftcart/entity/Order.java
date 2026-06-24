package in.swiftcart.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable=false , unique=true , length=50)
	private String orderNumber;
	
	@ManyToOne(fetch=FetchType.LAZY) //many order placed by one user
	@JoinColumn(name="user_id" , nullable=false) //foreign key made on this table it tells 
	private User user;
	
	@OneToMany(mappedBy="order", cascade=CascadeType.ALL, orphanRemoval=true)
	@Builder.Default
	private List<OrderItem>orderItems = new ArrayList<>();
	
	private Double totalAmmount;
	
	public final static String STATUS_CONFIRMED="CONFIRMED";
	public final static String STATUS_CANCELLED="CANCELLED";
	@Column(nullable=false , length=20)
	@Builder.Default
	private String status = STATUS_CONFIRMED;
	
	private String notes;
	
	private LocalDateTime orderDate;
	
	//helper method
	@PrePersist
	public void generateOrderNumber() {
		if(this.orderNumber==null) {
			this.orderNumber="ord"+UUID.randomUUID().toString().substring(0,8).toUpperCase();
		}
		if(this.orderDate==null) {
			this.orderDate = LocalDateTime.now();
		}
	}
	
	public void addOrderItem(OrderItem item) {
		orderItems.add(item); //to add item in the cartlist 
		item.setOrder(this);
	}
	public void calculateTotals() {
		double total=0.0;
		for(OrderItem item:orderItems) {
			total=total+item.getSubTotal();
		}
		this.totalAmmount=total;
	}

}
