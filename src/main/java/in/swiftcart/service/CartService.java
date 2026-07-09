package in.swiftcart.service;

import in.swiftcart.dtorequest.AddToCartRequestDTO;
import in.swiftcart.dtorequest.UpdateCartItemRequestDTO;
import in.swiftcart.dtoresponse.CartItemResponseDTO;
import in.swiftcart.dtoresponse.CartResponseDTO;

public interface CartService {

    CartResponseDTO getCartByUserId(Long userId);

    CartResponseDTO addItemToCart(Long userId, AddToCartRequestDTO addToCartRequestDTO);

    CartResponseDTO updateCartItem(Long userId, Long cartItemId, UpdateCartItemRequestDTO updateCartItemRequestDTO);

    CartResponseDTO removeItemFromCart(Long userId, Long cartItemId);

    CartResponseDTO clearCart(Long userId);

    CartItemResponseDTO getCartItem(Long userId, Long cartItemId);

    boolean isProductInCart(Long userId, Long productId);

    CartResponseDTO incrementItemQuantity(Long userId, Long productId, int quantity);

    CartResponseDTO decrementItemQuantity(Long userId, Long productId, int quantity);
}

