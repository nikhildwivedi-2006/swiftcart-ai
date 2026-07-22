package in.swiftcart.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import in.swiftcart.dtorequest.LoginRequestDTO;
import in.swiftcart.dtorequest.RegisterRequestDTO;
import in.swiftcart.dtoresponse.ApiResponseDTO;
import in.swiftcart.dtoresponse.LoginResponseDTO;
import in.swiftcart.dtoresponse.RegisterResponseDTO;
import in.swiftcart.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Validated
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponseDTO<LoginResponseDTO>> login(
            @Valid @RequestBody LoginRequestDTO request) {

        LoginResponseDTO response = authService.login(request);

        return ResponseEntity.ok(
                ApiResponseDTO.success(
                        "Login successful",
                        response
                )
        );
    }
    
    @PostMapping("/register")
    public ResponseEntity<ApiResponseDTO<RegisterResponseDTO>> register(
            @Valid @RequestBody RegisterRequestDTO request) {
        RegisterResponseDTO response = authService.register(request);
        return ResponseEntity.ok(
                ApiResponseDTO.success(
                        "Registration successful",
                        response
                )
        );
    }
}