package pt.home.services;

import org.springframework.http.ResponseEntity;
import pt.home.payload.ApiResponse;
import pt.home.payload.LoginRequest;
import pt.home.payload.SignUpRequest;

public interface AuthService {

    String authenticate(LoginRequest loginRequest);

    ResponseEntity<ApiResponse> register(SignUpRequest signUpRequest);

    ResponseEntity<ApiResponse> logout(String jwt) throws InterruptedException;
}
