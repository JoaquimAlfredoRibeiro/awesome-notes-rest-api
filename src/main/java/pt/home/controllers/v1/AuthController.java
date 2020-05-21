package pt.home.controllers.v1;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pt.home.payload.ApiResponse;
import pt.home.payload.JwtAuthenticationResponse;
import pt.home.payload.LoginRequest;
import pt.home.payload.SignUpRequest;
import pt.home.services.AuthService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping(AuthController.BASE_URL)
@RequiredArgsConstructor
public class AuthController {

    public static final String BASE_URL = "/oapi/auth";

    private final AuthService authService;

    @PostMapping("/signin")
    //authenticate user with valid login request
    public ResponseEntity<JwtAuthenticationResponse> authenticateUser(@Valid @RequestBody final LoginRequest loginRequest) {

        return ResponseEntity.ok(new JwtAuthenticationResponse(authService.authenticate(loginRequest)));
    }

    @PostMapping("/signup")
    //register user with valid signup request
    public ResponseEntity<ApiResponse> registerUser(@Valid @RequestBody final SignUpRequest signUpRequest) {

        return authService.register(signUpRequest);
    }

    @PostMapping("/logout")
    //add jwt to REDIS blacklist
    public ResponseEntity<ApiResponse> logout(HttpServletRequest request) throws InterruptedException {
        return authService.logout(getJwtFromRequest(request));
    }

    //isolate jwt from request
    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7, bearerToken.length());
        }
        return null;
    }
}