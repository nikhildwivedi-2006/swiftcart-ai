package in.swiftcart.service;

import org.springframework.stereotype.Service;

import in.swiftcart.dtorequest.LoginRequestDTO;
import in.swiftcart.dtoresponse.LoginResponseDTO;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

	@Override
	public LoginResponseDTO login(LoginRequestDTO loginRequestDTO) {
		
		
		//jwt logic 
		
		return null;
		
	}

	
	
	
}
