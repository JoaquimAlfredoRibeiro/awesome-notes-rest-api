package pt.home.security.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import pt.home.domain.auth.User;
import pt.home.instanceprovers.UserInstanceProvider;
import pt.home.repositories.UserRepository;

class CustomUserDetailsServiceTest {

	@Mock
	private UserRepository userRepository;

	@InjectMocks
	private CustomUserDetailsService customUserDetailsService;

	@BeforeEach
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	void loadUserByName_whenUserIsFound_returnsCreatedUser() {

		final User suppliedUser = new UserInstanceProvider().getInstance();

		when(userRepository.findByUsername(suppliedUser.getUsername())).thenReturn(Optional.of(suppliedUser));

		final UserDetails expectedUserDetails = customUserDetailsService.loadUserByUsername(suppliedUser.getUsername());

		assertAll(
				() -> assertThat(expectedUserDetails).isNotNull(),
				() -> assertThat(expectedUserDetails).isEqualToComparingOnlyGivenFields(
						suppliedUser,
						"firstName",
						"lastName",
						"username",
						"email"
				)
		);
	}

	@Test
	void loadUserByName_whenUserIsNotFound_throwsUserNotFoundException() {

		final User suppliedUser = new UserInstanceProvider().getInstance();

		when(userRepository.findByUsername(suppliedUser.getUsername())).thenReturn(Optional.empty());

		final Exception exceptionThrown = assertThrows(UsernameNotFoundException.class,
				() -> customUserDetailsService.loadUserByUsername(suppliedUser.getUsername()));

		assertThat(exceptionThrown.getMessage()).contains(suppliedUser.getUsername());

	}

	@Test
	void loadUserById_whenUserIsFound_returnsCreatedUser() {

		final User suppliedUser = new UserInstanceProvider().getInstance();

		when(userRepository.findById(suppliedUser.getId())).thenReturn(Optional.of(suppliedUser));

		final UserDetails expectedUserDetails = customUserDetailsService.loadUserById(suppliedUser.getId());

		assertAll(
				() -> assertThat(expectedUserDetails).isNotNull(),
				() -> assertThat(expectedUserDetails).isEqualToComparingOnlyGivenFields(
						suppliedUser,
						"firstName",
						"lastName",
						"username",
						"email"
				)
		);
	}

	@Test
	void loadUserById_whenUserIsNotFound_throwsUserNotFoundException() {

		final User suppliedUser = new UserInstanceProvider().getInstance();

		when(userRepository.findById(suppliedUser.getId())).thenReturn(Optional.empty());

		final Exception exceptionThrowed = assertThrows(UsernameNotFoundException.class,
				() -> customUserDetailsService.loadUserById(suppliedUser.getId()));

		assertThat(exceptionThrowed.getMessage()).isNotBlank();
	}
}
