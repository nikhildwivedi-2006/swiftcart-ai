package in.swiftcart.service;

import java.util.List;

import in.swiftcart.dtorequest.UpdateUserRequestDTO;
import in.swiftcart.dtorequest.UserRequestDTO;
import in.swiftcart.dtoresponse.UserResponseDTO;

public interface UserService {
	
	UserResponseDTO createUser(UserRequestDTO userRequestDTO);
	UserResponseDTO getUserById(Long id);
	UserResponseDTO getuserByEmail(String email);
	
	List<UserResponseDTO> getAllUsers();
	List<UserResponseDTO> getActiveUsers();
	
	UserResponseDTO updateUser(Long id, UpdateUserRequestDTO userRequest);
	void activateUser(Long id);
	void deActivateUser(Long id);
	List<UserResponseDTO> searchUser(String Keyword);
	boolean existsByEmail(String email);
}

















