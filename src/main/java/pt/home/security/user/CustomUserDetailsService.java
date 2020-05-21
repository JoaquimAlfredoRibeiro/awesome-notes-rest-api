package pt.home.security.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pt.home.domain.auth.User;
import pt.home.repositories.UserRepository;

@Service
@RequiredArgsConstructor
//Custom UserDetailsService implementation for authentication
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    //load userDetails by username
    public UserDetails loadUserByUsername(final String username) {

        final User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));

        return UserPrincipal.create(user);
    }

    @Transactional
    // This method is used by JWTAuthenticationFilter
    public UserDetails loadUserById(final Long id) {

        final User user = userRepository.findById(id).orElseThrow(
                () -> new UsernameNotFoundException("User not found with supplied Id")
        );

        return UserPrincipal.create(user);
    }
}

