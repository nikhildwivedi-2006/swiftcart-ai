package in.swiftcart.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import in.swiftcart.dtorequest.AddToCartRequestDTO;
import in.swiftcart.dtorequest.UpdateCartItemRequestDTO;
import in.swiftcart.dtoresponse.CartItemResponseDTO;
import in.swiftcart.dtoresponse.CartResponseDTO;
import in.swiftcart.entity.Cart;
import in.swiftcart.entity.CartItem;
import in.swiftcart.entity.Product;
import in.swiftcart.exception.InsufficientStockException;
import in.swiftcart.exception.ResourceNotFoundException;
import in.swiftcart.repository.CartItemRepository;
import in.swiftcart.repository.CartRepository;
import in.swiftcart.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class CartServiceImpl implements CartService {

	 private  final CartRepository cartRepository;
	    private final   CartItemRepository cartItemRepository;
	    private final ProductRepository productRepository;

	// ==================== HELPERS ====================

	    private Cart getCart(Long userId) {

	        Optional<Cart> optional = cartRepository.findByUserIdWithItems(userId);

	        if (!optional.isPresent()) {
	            throw new ResourceNotFoundException("Cart", "userId", userId);
	        }

	        return optional.get();
	    }

	    private Product findProductById(Long productId) {

	        Optional<Product> optional = productRepository.findById(productId);

	        if (!optional.isPresent()) {
	            throw new ResourceNotFoundException("Product", "id", productId);
	        }

	        return optional.get();
	    }

	    private CartItem findCartItemById(Long cartItemId) {

	        Optional<CartItem> optional = cartItemRepository.findById(cartItemId);

	        if (!optional.isPresent()) {
	            throw new ResourceNotFoundException("CartItem", "id", cartItemId);
	        }

	        return optional.get();
	    }

	    private void validateProductAvailability(Product product, int requestedQuantity) {

	        if (!product.getIsAvailable()) {
	            throw new InsufficientStockException(product.getName() + " is not available");
	        }

	        if (product.getStockQuantity() < requestedQuantity) {
	            throw new InsufficientStockException(
	                    "Insufficient stock for " + product.getName() +
	                            ". Available: " + product.getStockQuantity() +
	                            ", Requested: " + requestedQuantity);
	        }
	    }

	    private CartResponseDTO mapToCartResponseDTO(Cart cart) {

	        List<CartItemResponseDTO> items = new ArrayList<CartItemResponseDTO>();

	        for (CartItem cartItem : cart.getCartItems()) {
	            items.add(mapToCartItemResponseDTO(cartItem));
	        }

	        return CartResponseDTO.builder()
	                .id(cart.getId())
	                .userId(cart.getUser().getId())
	                .userName(cart.getUser().getFullName())
	                .items(items)
	                .totalItems(cart.getTotalItems())
	                .totalAmount(cart.getTotalAmmount())
	                .createdAt(cart.getCreatedAt())
	                .updatedAt(cart.getUpdatedAt())
	                .build();
	    }

	    private CartItemResponseDTO mapToCartItemResponseDTO(CartItem cartItem) {

	        Product product = cartItem.getProduct();

	        return CartItemResponseDTO.builder()
	                .id(cartItem.getId())
	                .productId(product.getId())
	                .productName(product.getName())
	                .productImage(product.getImageUrl())
	                .productSku(product.getSku())
	                .unitPrice(product.getPrice())
	                .quantity(cartItem.getQuantity())
	                .subTotal(cartItem.getSubTotal())
	                .availabelStock(product.getStockQuantity())
	                .addedAt(cartItem.getAddedAt())
	                .build();
	    }

		@Override
		public CartResponseDTO getCartByUserId(Long userId) {
			Cart cart = getCart(userId);
	        return mapToCartResponseDTO(cart);
		}

		@Override
		public CartResponseDTO addItemToCart(Long userId, AddToCartRequestDTO addToCartRequestDTO) {
			 Cart cart = getCart(userId);
		        //fetch the product
		        Product product = findProductById(addToCartRequestDTO.getProductId());

		        //validate the qty
		        validateProductAvailability(product, addToCartRequestDTO.getQuantity());

		        Optional<CartItem> existing = cartItemRepository
		                .findByCartIdAndProductId(cart.getId(), product.getId());

		        if (existing.isPresent()) {

		            CartItem item = existing.get();

		            int newQty = item.getQuantity() + addToCartRequestDTO.getQuantity();
		            validateProductAvailability(product, newQty);

		            item.setQuantity(newQty);
		            item.calculateSubTotal();
		            cartRepository.save(cart);

		        } else {
		            CartItem item = CartItem.builder()
		                    .product(product)
		                    .quantity(addToCartRequestDTO.getQuantity())
		                    .unitPrice(product.getPrice())
		                    .build();
		            item.calculateSubTotal();
		            cart.addCartItem(item); // Cascade handles save
		        }

		        cart.recalculateTotals();

		        cartRepository.save(cart); // ⚠️ Not required, but kept

		        return mapToCartResponseDTO(cart);
		}

		@Override
	    public CartResponseDTO updateCartItem(Long userId, Long cartItemId, UpdateCartItemRequestDTO dto) {

	        Cart cart = getCart(userId);
	        CartItem cartItem = findCartItemById(cartItemId);

	        if (!cartItem.getCart().getId().equals(cart.getId())) {
	            throw new ResourceNotFoundException("CartItem", "id", cartItemId);
	        }

	        validateProductAvailability(cartItem.getProduct(), dto.getQuantity());

	        cartItem.setQuantity(dto.getQuantity());
	        cartItem.calculateSubTotal();

	        cart.recalculateTotals();

	        cartRepository.save(cart); // ⚠️ Not required, but kept

	        return mapToCartResponseDTO(cart);
	    }

	    @Override
	    public CartResponseDTO removeItemFromCart(Long userId, Long cartItemId) {

	        Cart cart = getCart(userId);
	        CartItem cartItem = findCartItemById(cartItemId);

	        if (!cartItem.getCart().getId().equals(cart.getId())) {
	            throw new ResourceNotFoundException("CartItem", "id", cartItemId);
	        }

	        cart.removeCartItem(cartItem); // orphanRemoval handles delete

	        cart.recalculateTotals();

	        cartRepository.save(cart); // ⚠️ Not required, but kept

	        return mapToCartResponseDTO(cart);
	    }

	    @Override
	    public CartResponseDTO clearCart(Long userId) {

	        Cart cart = getCart(userId);

	        cart.clearCart(); // orphanRemoval deletes all items

	        cartRepository.save(cart); // ⚠️ Not required, but kept

	        return mapToCartResponseDTO(cart);
	    }

	    @Override
	    public CartItemResponseDTO getCartItem(Long userId, Long cartItemId) {

	        Cart cart = getCart(userId);
	        CartItem cartItem = findCartItemById(cartItemId);

	        if (!cartItem.getCart().getId().equals(cart.getId())) {
	            throw new ResourceNotFoundException("CartItem", "id", cartItemId);
	        }

	        return mapToCartItemResponseDTO(cartItem);
	    }

	    @Override
	    public boolean isProductInCart(Long userId, Long productId) {

	        Optional<Cart> cartOptional = cartRepository.findByUserId(userId);

	        if (!cartOptional.isPresent()) {
	            return false;
	        }

	        Cart cart = cartOptional.get();

	        return cartItemRepository.existsByCartIdAndProductId(cart.getId(), productId);
	    }

	    @Override
	    public CartResponseDTO incrementItemQuantity(Long userId, Long productId, int quantity) {

	        if (quantity <= 0) {
	            throw new IllegalArgumentException("Quantity must be greater than 0");
	        }

	        Cart cart = getCart(userId);

	        Optional<CartItem> optional = cartItemRepository
	                .findByCartIdAndProductId(cart.getId(), productId);

	        if (!optional.isPresent()) {
	            throw new ResourceNotFoundException("CartItem", "productId", productId);
	        }

	        CartItem cartItem = optional.get();

	        int newQty = cartItem.getQuantity() + quantity;

	        validateProductAvailability(cartItem.getProduct(), newQty);

	        cartItem.setQuantity(newQty);
	        cartItem.calculateSubTotal();

	        cart.recalculateTotals();

	        cartRepository.save(cart); // ⚠️ Not required, but kept

	        return mapToCartResponseDTO(cart);
	    }

	    @Override
	    public CartResponseDTO decrementItemQuantity(Long userId, Long productId, int quantity) {

	        if (quantity <= 0) {
	            throw new IllegalArgumentException("Quantity must be greater than 0");
	        }

	        Cart cart = getCart(userId);

	        Optional<CartItem> optional = cartItemRepository
	                .findByCartIdAndProductId(cart.getId(), productId);

	        if (!optional.isPresent()) {
	            throw new ResourceNotFoundException("CartItem", "productId", productId);
	        }

	        CartItem cartItem = optional.get();

	        int newQty = cartItem.getQuantity() - quantity;

	        if (newQty <= 0) {
	            cart.removeCartItem(cartItem); // orphanRemoval handles delete
	        } else {
	            cartItem.setQuantity(newQty);
	            cartItem.calculateSubTotal();
	        }

	        cart.recalculateTotals();

	        cartRepository.save(cart); // ⚠️ Not required, but kept

	        return mapToCartResponseDTO(cart);
	    }
	
	
}
