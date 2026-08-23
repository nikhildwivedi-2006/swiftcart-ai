package in.swiftcart.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import in.swiftcart.dtorequest.UpdateUserRequestDTO;
import in.swiftcart.dtorequest.UserRequestDTO;
import in.swiftcart.dtoresponse.UserResponseDTO;
import in.swiftcart.entity.Cart;
import in.swiftcart.entity.User;
import in.swiftcart.exception.DuplicateResourceException;
import in.swiftcart.exception.ResourceNotFoundException;
import in.swiftcart.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class  UserServiceImpl implements UserService{
	
	private final UserRepository userRepo;
	private final PasswordEncoder passwordEncoder;
	
	
	
	
	@Override
    public UserResponseDTO createUser(UserRequestDTO userRequestDTO) {
        // Check if email already exists
        if (existsByEmail(userRequestDTO.getEmail())) {
            throw new DuplicateResourceException("User", "email", userRequestDTO.getEmail());
        }

        // Create user entity
        User user = User.builder()
                .fullName(userRequestDTO.getFullName())
                .email(userRequestDTO.getEmail())
                .password(passwordEncoder.encode(userRequestDTO.getPassword())) //encoded password
                .phone(userRequestDTO.getPhone())
                .address(userRequestDTO.getAddress())
                .build();

        // Create cart for user
        Cart cart = Cart.builder()
                .user(user)
                .build();
        user.setCart(cart);

        User savedUser = userRepo.save(user);
        return mapToResponse(savedUser);
    }

	
	
	//entity user converted into dto also known as mapper
	private UserResponseDTO mapToResponse(User user) { 
		return UserResponseDTO.builder()
				.id(user.getId())
				.fullName(user.getFullName())
				.email(user.getEmail())
				.phone(user.getPhone())
				.address(user.getAddress())
				.isActive(user.getIsActive())
				.build();
	}
	

	private User findUserById(Long id) {
		Optional<User> opt = userRepo.findById(id);
		if(opt.isPresent()) {
			return opt.get();
		}
		
		throw new ResourceNotFoundException("user" , "id" ,id);
	}


	@Override
	public UserResponseDTO getUserById(Long id) {
		 User user = findUserById(id);
	        return mapToResponse(user);
	}


	@Override
	public UserResponseDTO getuserByEmail(String email) {
		 Optional<User> userOptional = userRepo.findByEmail(email);
	        if (userOptional.isPresent()) {
	            return mapToResponse(userOptional.get());
	        } else {
	            throw new ResourceNotFoundException("User", "email", email);
	        }
	}


	@Override
	public List<UserResponseDTO> getAllUsers() {
		List<User> users = userRepo.findAll();
        List<UserResponseDTO> responseList = new ArrayList<>();
        for (User user : users) {
            responseList.add(mapToResponse(user));
        }
        return responseList;
	}


	@Override
	public List<UserResponseDTO> getActiveUsers() {
		List<User> activeUsers = userRepo.findByIsActiveTrue();
        List<UserResponseDTO> responseList = new ArrayList<>();
        for (User user : activeUsers) {
            responseList.add(mapToResponse(user));
        }
        return responseList;
	}


	@Override
	public UserResponseDTO updateUser(Long id, UpdateUserRequestDTO userRequest) {
		
		 User user = findUserById(id);
		    
		    if (userRequest.getFullName() == null &&
		    	userRequest.getPassword() == null &&
		    	userRequest.getPhone() == null &&
		    	userRequest.getEmail() == null &&
		    	userRequest.getAddress() == null) {

		        throw new IllegalArgumentException("At least one field must be provided for update");
		    }

		    
		    if (userRequest.getFullName() != null) {
		        if (userRequest.getFullName().isBlank()) {
		            throw new IllegalArgumentException("Full name cannot be blank");
		        }
		        user.setFullName(userRequest.getFullName().trim());
		    }

		   
		    if (userRequest.getPassword() != null) {
		        if (userRequest.getPassword().isBlank()) {
		            throw new IllegalArgumentException("Password cannot be blank");
		        }
		        user.setPassword(userRequest.getPassword()); // Later you can encode
		    }

		    
		    if (userRequest.getPhone() != null) {
		        if (userRequest.getPhone().isBlank()) {
		            throw new IllegalArgumentException("Phone cannot be blank");
		        }
		        user.setPhone(userRequest.getPhone());
		    }

		   
		    if (userRequest.getEmail() != null) {
		        if (userRequest.getEmail().isBlank()) {
		            throw new IllegalArgumentException("Email cannot be blank");
		        }

		        String newEmail = userRequest.getEmail().trim();

		        
		        if (!newEmail.equals(user.getEmail()) &&
		                userRepo.existsByEmail(newEmail)) {
		            throw new DuplicateResourceException("User", "email", newEmail);
		        }

		        user.setEmail(newEmail);
		    }

		  
		    if (userRequest.getAddress() != null) {
		        if (userRequest.getAddress().isBlank()) {
		            throw new IllegalArgumentException("Address cannot be blank");
		        }
		        user.setAddress(userRequest.getAddress().trim());
		    }
		    User updatedUser = userRepo.save(user);
		    return mapToResponse(updatedUser);
	}


	@Override
	public void activateUser(Long id) {
		User user = findUserById(id);
		user.setIsActive(true);
		userRepo.save(user);
	}


	@Override
	public void deActivateUser(Long id) {
		User user = findUserById(id);
		user.setIsActive(false);
		userRepo.save(user);
		
	}


	@Override
	public List<UserResponseDTO> searchUser(String Keyword) {
		List<User> users = userRepo.searchByNameOrEmail(Keyword);
        List<UserResponseDTO> responseList = new ArrayList<>();
        for (User user : users) {
            responseList.add(mapToResponse(user));
        }
        return responseList;
	}


	@Override
	public boolean existsByEmail(String email) {
		return userRepo.existsByEmail(email);
	}
}
