package in.swiftcart.service;

import in.swiftcart.dtorequest.LoginRequestDTO;
import in.swiftcart.dtorequest.RegisterRequestDTO;
import in.swiftcart.dtoresponse.LoginResponseDTO;
import in.swiftcart.dtoresponse.RegisterResponseDTO;

public interface AuthService {

	LoginResponseDTO login(LoginRequestDTO loginRequestDTO);
	
	RegisterResponseDTO register(RegisterRequestDTO registerRequestDTO);
	
}
