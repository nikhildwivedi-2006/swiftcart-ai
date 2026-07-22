package in.swiftcart.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import in.swiftcart.dtorequest.LoginRequestDTO;
import in.swiftcart.dtorequest.RegisterRequestDTO;
import in.swiftcart.dtoresponse.LoginResponseDTO;
import in.swiftcart.dtoresponse.RegisterResponseDTO;
import in.swiftcart.entity.Cart;
import in.swiftcart.entity.User;
import in.swiftcart.repository.CartRepository;
import in.swiftcart.repository.UserRepository;
import in.swiftcart.security.CustomUserDetails;
import in.swiftcart.security.JwtService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CartRepository cartRepository;

    @Override
    public LoginResponseDTO login(LoginRequestDTO loginRequestDTO) {

        // verification of email & password
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequestDTO.getEmail(),
                        loginRequestDTO.getPassword()
                )
        );

        // authentication.getPrincipal() return the login user
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        // generate jwt token from user details
        String token = jwtService.generateToken(userDetails);

        // send the token to the client or frontend
        User user = userRepository.findByEmail(loginRequestDTO.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        return LoginResponseDTO.builder()
                .token(token)
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .address(user.getAddress())
                .role(user.getRole().name())
                .build();
    }

    @Override
    public RegisterResponseDTO register(RegisterRequestDTO registerRequestDTO) {

        // check if email already exists
        if (userRepository.existsByEmail(registerRequestDTO.getEmail())) {
            throw new RuntimeException("Email already registered");
        }

        // build user entity
        User user = User.builder()
                .fullName(registerRequestDTO.getName())
                .email(registerRequestDTO.getEmail())
                .password(passwordEncoder.encode(registerRequestDTO.getPassword()))
                .phone(registerRequestDTO.getContactNumber())
                .build();

        // save user
        User savedUser = userRepository.save(user);
        
     // create empty cart for new user
        Cart cart = Cart.builder()
                .user(savedUser)
                .totalItems(0)
                .totalAmmount(0.0)
                .build();

        cartRepository.save(cart);


        // generate token
        CustomUserDetails userDetails = new CustomUserDetails(savedUser);
        String token = jwtService.generateToken(userDetails);

        // return response
        return RegisterResponseDTO.builder()
                .id(savedUser.getId())
                .token(token)
                .fullName(savedUser.getFullName())
                .email(savedUser.getEmail())
                .phone(savedUser.getPhone())
                .address(savedUser.getAddress())
                .build();
    }
}