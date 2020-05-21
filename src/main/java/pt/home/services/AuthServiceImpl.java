package pt.home.services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pt.home.domain.auth.ERole;
import pt.home.domain.auth.Role;
import pt.home.domain.auth.User;
import pt.home.payload.ApiResponse;
import pt.home.payload.LoginRequest;
import pt.home.payload.SignUpRequest;
import pt.home.repositories.RoleRepository;
import pt.home.repositories.UserRepository;
import pt.home.security.jwt.JwtTokenProvider;

import java.util.*;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    //Constants for Response
    public static final String EMAIL_USED = "Email not available";
    public static final String USER_NAME_NOT_AVAILABLE = "Username not available";
    public static final String PASSWORD_MATCH = "Passwords must match";
    public static final String ROLE_NOT_FOUND = "Role not found";
    public static final String USER_REGISTRY_SUCESSFUL = "User Registered Successfully";
    public static final String LOGOUT_SUCESSFUL = "Logout Successful";

    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Autowired
    private RedisTemplate<String, Object> template;

    @Override
    //Authenticate Login Request and return generated Jwt
    public String authenticate(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        return tokenProvider.generateToken(authentication);
    }

    @Override
    //Register new user and return general ApiResponse
    public ResponseEntity<ApiResponse> register(SignUpRequest signUpRequest) {
        //email must be unique
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return new ResponseEntity<>(new ApiResponse(false, EMAIL_USED),
                    HttpStatus.BAD_REQUEST);
        }

        //username must be unique
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return new ResponseEntity<>(new ApiResponse(false, USER_NAME_NOT_AVAILABLE),
                    HttpStatus.BAD_REQUEST);
        }

        //password and password confirmation must match
        if (!signUpRequest.getPassword().equals(signUpRequest.getRepeatPassword())) {
            return new ResponseEntity<>(new ApiResponse(false, PASSWORD_MATCH),
                    HttpStatus.BAD_REQUEST);
        }

        // Creating user's account
        User user = new User(signUpRequest.getFirstName(), signUpRequest.getLastName(), signUpRequest.getUsername(),
                signUpRequest.getEmail(), signUpRequest.getPassword());

        //set encoded password
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        //Add roles to new user
        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();

        //if no roles are attributed, by default every new user has ROLE_USER
        if (strRoles == null) {
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException(ROLE_NOT_FOUND));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException(ROLE_NOT_FOUND));
                        roles.add(adminRole);
                        break;
                    default:
                        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException(ROLE_NOT_FOUND));
                        roles.add(userRole);
                }
            });
        }

        user.setRoles(roles);
        userRepository.save(user);

        return ResponseEntity.ok(new ApiResponse(true, USER_REGISTRY_SUCESSFUL));
    }

    @Override
    //add jwt to REDIS blacklist
    //use to ensure user logout on server
    public ResponseEntity<ApiResponse> logout(final String jwt) {
        template.opsForValue().set(jwt, "blacklistedJwt");

        return ResponseEntity.ok(new ApiResponse(true, LOGOUT_SUCESSFUL));
    }
}
