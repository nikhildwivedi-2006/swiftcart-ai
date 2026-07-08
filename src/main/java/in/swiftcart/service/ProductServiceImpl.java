package in.swiftcart.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import in.swiftcart.dtorequest.ProductRequestDTO;
import in.swiftcart.dtoresponse.PageResponseDTO;
import in.swiftcart.dtoresponse.ProductResponseDTO;
import in.swiftcart.entity.Product;
import in.swiftcart.exception.DuplicateResourceException;
import in.swiftcart.exception.ResourceNotFoundException;
import in.swiftcart.repository.ProductRepository;
import jakarta.transaction.Transactional;

@Service

@Transactional
public class ProductServiceImpl implements ProductService {

	 private final ProductRepository productRepository;

	
	@Autowired
	public ProductServiceImpl(ProductRepository productRepository) {
		super();
		this.productRepository = productRepository;
	}

	
	//create product
	    @Override
	    public ProductResponseDTO createProduct(ProductRequestDTO productRequestDTO) {
	        if (productRequestDTO.getSku() != null && productRepository.existsBySku(productRequestDTO.getSku())) {
	            throw new DuplicateResourceException("Product", "SKU", productRequestDTO.getSku());
	        }

	        Product product = Product.builder()
	                .name(productRequestDTO.getName())
	                .description(productRequestDTO.getDescription())
	                .price(productRequestDTO.getPrice() != null ? productRequestDTO.getPrice() : 0.0)
	                .stockQuantity(productRequestDTO.getStockQuantity())
	                .category(productRequestDTO.getCategory())
	                .brand(productRequestDTO.getBrand())
	                .imageUrl(productRequestDTO.getImageUrl())
	                .sku(productRequestDTO.getSku())
	                .isAvailable(productRequestDTO.getIsAvailable() != null ? productRequestDTO.getIsAvailable() : true)
	                .build();

	        Product savedProduct = productRepository.save(product);
	        return mapToResponseDTO(savedProduct);
	    }

	    //get product by id
	    @Override
	    public ProductResponseDTO getProductById(Long id) {
	        Product product = findProductById(id);
	        return mapToResponseDTO(product);
	    }

	    //get product by sku
	    @Override
	    public ProductResponseDTO getProductBySku(String sku) {
	        Optional<Product> productOptional = productRepository.findBySku(sku);
	        if (productOptional.isPresent()) {
	            return mapToResponseDTO(productOptional.get());
	        } else {
	            throw new ResourceNotFoundException("Product", "SKU", sku);
	        }
	    }

	    //get all products
	    @Override
	    public List<ProductResponseDTO> getAllProducts() {
	        List<Product> products = productRepository.findAll();
	        List<ProductResponseDTO> responseList = new ArrayList<>();
	        for (Product product : products) {
	            responseList.add(mapToResponseDTO(product));
	        }
	        return responseList;
	    }

	    //get all product paginated
	    @Override
	    public PageResponseDTO<ProductResponseDTO> getAllProductsPaginated(int page, int size, String sortBy,
	            String sortDir) {
	        Pageable pageable = createPageable(page, size, sortBy, sortDir);
	        Page<Product> productPage = productRepository.findAll(pageable);
	        return mapToPageResponse(productPage);
	    }

	    //get available products
	    @Override
	    public List<ProductResponseDTO> getAvailableProducts() {
	        List<Product> products = productRepository.findByIsAvailableTrue();
	        List<ProductResponseDTO> responseList = new ArrayList<>();
	        for (Product product : products) {
	            responseList.add(mapToResponseDTO(product));
	        }
	        return responseList;
	    }

	    //get product by category
	    @Override
	    public List<ProductResponseDTO> getProductsByCategory(String category) {
	        List<Product> products = productRepository.findByCategoryIgnoreCase(category);
	        List<ProductResponseDTO> responseList = new ArrayList<>();
	        for (Product product : products) {
	            responseList.add(mapToResponseDTO(product));
	        }
	        return responseList;
	    }

	    //get products by price
	    @Override
	    public List<ProductResponseDTO> getProductsByPriceRange(double minPrice, double maxPrice) {
	        List<Product> products = productRepository.findByPriceBetween(minPrice, maxPrice);
	        List<ProductResponseDTO> responseList = new ArrayList<>();
	        for (Product product : products) {
	            responseList.add(mapToResponseDTO(product));
	        }
	        return responseList;
	    }

	    //search products
	    @Override
	    public PageResponseDTO<ProductResponseDTO> searchProducts(String keyword, int page, int size) {
	        Pageable pageable = PageRequest.of(page, size);
	        Page<Product> productPage = productRepository.searchProducts(keyword, pageable);
	        return mapToPageResponse(productPage);
	    }

	    //update product
	    @Override
	    public ProductResponseDTO updateProduct(Long id, ProductRequestDTO productRequestDTO) {
	        Product product = findProductById(id);
	        
	        if(productRequestDTO.getName()==null
	        		&& productRequestDTO.getDescription()==null
	        		&& productRequestDTO.getPrice()==null
	        		&& productRequestDTO.getStockQuantity()==null
	        		&& productRequestDTO.getCategory()==null
	        		&& productRequestDTO.getBrand()==null
	        		&& productRequestDTO.getImageUrl()==null
	        		&& productRequestDTO.getSku()==null
	        		&& productRequestDTO.getIsAvailable()==null) {
	        	throw new IllegalArgumentException("At least one field should be provided for updation");
	        }
	        
	        if(productRequestDTO.getName()!= null) {
	        	if(productRequestDTO.getName().isBlank()) {
	        		throw new IllegalArgumentException("product name cannot be left blank ");
	        	}
	        	product.setName(productRequestDTO.getName().trim());
	        }

	        if(productRequestDTO.getDescription()!= null) {
	        	if(productRequestDTO.getDescription().isBlank()) {
	        		throw new IllegalArgumentException("product description cannot be left blank ");
	        	}
	        	product.setDescription(productRequestDTO.getDescription().trim());
	        }
	        
	        if(productRequestDTO.getPrice()!= null) {
	        	if(productRequestDTO.getPrice()<0) {
	        		throw new IllegalArgumentException("product price cannot be negative ");
	        	}
	        	product.setPrice(productRequestDTO.getPrice());
	        }
	        
	        if(productRequestDTO.getStockQuantity()!= null) {
	        	if(productRequestDTO.getStockQuantity()<0) {
	        		throw new IllegalArgumentException("product quantity cannot be negative ");
	        	}
	        	product.setStockQuantity(productRequestDTO.getStockQuantity());
	        }
	  
	        if(productRequestDTO.getCategory()!= null) {
	        	if(productRequestDTO.getCategory().isBlank()) {
	        		throw new IllegalArgumentException("product category  cannot be left blank ");
	        	}
	        	product.setCategory(productRequestDTO.getCategory().trim());
	        }
	        
	        if(productRequestDTO.getBrand()!= null) {
	        	if(productRequestDTO.getBrand().isBlank()) {
	        		throw new IllegalArgumentException("product brand cannot be left blank ");
	        	}
	        	product.setBrand(productRequestDTO.getBrand().trim());
	        }

	        if(productRequestDTO.getImageUrl()!= null) {
	        	if(productRequestDTO.getImageUrl().isBlank()) {
	        		throw new IllegalArgumentException("product ImageURL cannot be left blank ");
	        	}
	        	product.setImageUrl(productRequestDTO.getImageUrl().trim());
	        }
	        
	        if(productRequestDTO.getSku()!= null) {
	        	if(productRequestDTO.getSku().isBlank()) {
	        		throw new IllegalArgumentException("product sku cannot be left blank ");
	        	}
	        	String sku= productRequestDTO.getSku().trim();
	        	
	        	if(!sku.equals(product.getSku()) 
		                && productRepository.existsBySku(sku)) {
	        		  throw new DuplicateResourceException("Product", "SKU", sku);
	        	}
	        	product.setSku(sku);
	        }

	        if (productRequestDTO.getIsAvailable() != null) {
	            product.setIsAvailable(productRequestDTO.getIsAvailable());
	        }
	        Product updatedProduct = productRepository.save(product);
	        return mapToResponseDTO(updatedProduct);

	      
	    }

	    //update stock
	    @Override
	    public void updateStock(Long productId, Integer quantity) {
	        Product product = findProductById(productId);
	        int newStock = product.getStockQuantity() + quantity;
	        if (newStock < 0) {
	            throw new IllegalArgumentException(
	                    "Stock quantity cannot be negative. Current stock: " + product.getStockQuantity());
	        }
	        product.setStockQuantity(newStock);
	        productRepository.save(product);
	    }

	    //get low stock products
	    @Override
	    public List<ProductResponseDTO> getLowStockProducts(Integer threshold) {
	    	if(threshold<0) {
	    		throw new IllegalArgumentException("Threshold cannot be negative:" +threshold);
	    	}
	        List<Product> productList = productRepository.findLowStockProducts(threshold);
	        List<ProductResponseDTO> responseList = new ArrayList<>();
	        for (Product product : productList) {
	            responseList.add(mapToResponseDTO(product));
	        }
	        return responseList;
	    }

	    //exists by sku
	    @Override
	    public boolean existsBySku(String sku) {
	        return productRepository.existsBySku(sku);
	    }

	    // Helper: find product by ID
	    private Product findProductById(Long id) {
	        Optional<Product> productOptional = productRepository.findById(id);
	        if (productOptional.isPresent()) {
	            return productOptional.get();
	        } else {
	            throw new ResourceNotFoundException("Product", "id", id);
	        }
	    }

	    // Helper: create Pageable
	    private Pageable createPageable(int page, int size, String sortBy, String sortDir) {
	        Sort sort;
	        if (sortDir.equalsIgnoreCase("desc")) {
	            sort = Sort.by(sortBy).descending();
	        } else {
	            sort = Sort.by(sortBy).ascending();
	        }
	        return PageRequest.of(page, size, sort);
	    }

	    // Helper: map Product to ProductResponseDTO
	    private ProductResponseDTO mapToResponseDTO(Product product) {
	        return ProductResponseDTO.builder()
	                .id(product.getId())
	                .name(product.getName())
	                .description(product.getDescription())
	                .price(product.getPrice())
	                .stockQuantity(product.getStockQuantity())
	                .category(product.getCategory())
	                .brand(product.getBrand())
	                .imageUrl(product.getImageUrl())
	                .sku(product.getSku())
	                .isAvailable(product.getIsAvailable())
	                .inStock(product.getStockQuantity() > 0)
	                .build();
	    }

	    // Helper: map Page to PageResponseDTO
	    private PageResponseDTO<ProductResponseDTO> mapToPageResponse(Page<Product> productPage) {
	        List<ProductResponseDTO> products = new ArrayList<>();
	        for (Product product : productPage.getContent()) {
	            products.add(mapToResponseDTO(product));
	        }

	        return PageResponseDTO.<ProductResponseDTO>builder()
	                .content(products)
	                .pageNumber(productPage.getNumber())
	                .pageSize(productPage.getSize())
	                .totalElements(productPage.getTotalElements())
	                .totalPages(productPage.getTotalPages())
	                .first(productPage.isFirst())
	                .last(productPage.isLast())
	                .hasNext(productPage.hasNext())
	                .hasPrevious(productPage.hasPrevious())
	                .build();
	    }
	
}
