package in.swiftcart.service;

import in.swiftcart.dtorequest.LoginRequestDTO;
import in.swiftcart.dtoresponse.LoginResponseDTO;

public interface AuthService {

	LoginResponseDTO login(LoginRequestDTO loginRequestDTO);
}
