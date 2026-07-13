package in.swiftcart.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import in.swiftcart.dtorequest.LoginRequestDTO;
import in.swiftcart.dtoresponse.LoginResponseDTO;
import in.swiftcart.security.CustomUserDetails;
import in.swiftcart.security.JwtService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

	private final AuthenticationManager authenticationManager;
	private final JwtService jwtService;
	
	
	@Override
	public LoginResponseDTO login(LoginRequestDTO loginRequestDTO) {
		
		//verification of email & password
		Authentication authentication = authenticationManager.authenticate(
                 new UsernamePasswordAuthenticationToken(
                		 loginRequestDTO.getEmail(),
                		 loginRequestDTO.getPassword()
                		 )
                 );
		
		//authentication.getPrincipal() return the login user 
		CustomUserDetails userDetails= (CustomUserDetails) authentication.getPrincipal();
		
		//generate jwt token from user details
		String token = jwtService.generateToken(userDetails);
		
		//send the token to the client or frontend 
		return LoginResponseDTO.builder()
				.token(token)
				.build();
		
	}
}
