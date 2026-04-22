package com.sky.user.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sky.user.domain.exception.UserNotFoundException;
import com.sky.user.domain.exception.UsernameAlreadyTakenException;
import com.sky.user.domain.model.User;
import com.sky.user.domain.repository.UserRepository;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

	@Mock
	private UserRepository userRepository;

	@Mock
	private PasswordEncoder passwordEncoder;

	private UserService userService;

	@BeforeEach
	void setUp() {
		userService = new UserService(userRepository, passwordEncoder);
	}

	@Test
    void createUser_encodesPasswordAndSaves() {
        when(userRepository.findByUsername("alice")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("raw")).thenReturn("enc");
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        User created = userService.createUser("alice", "a@b.c", "raw");

        assertThat(created.getUsername()).isEqualTo("alice");
        assertThat(created.getEmail()).isEqualTo("a@b.c");
        assertThat(created.getPassword()).isEqualTo("enc");
        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());
        assertThat(captor.getValue().getPassword()).isEqualTo("enc");
    }

	@Test
    void createUser_throwsWhenUsernameTaken() {
        when(userRepository.findByUsername("alice"))
                .thenReturn(Optional.of(new User(UUID.randomUUID(), "alice", "x@y.z", "p")));

        assertThatThrownBy(() -> userService.createUser("alice", "a@b.c", "raw"))
                .isInstanceOf(UsernameAlreadyTakenException.class);
    }

	@Test
	void getUserById_throwsWhenMissing() {
		UUID id = UUID.randomUUID();
		when(userRepository.findById(id)).thenReturn(Optional.empty());

		assertThatThrownBy(() -> userService.getUserById(id)).isInstanceOf(UserNotFoundException.class);
	}

	@Test
	void updateUser_updatesValuesAndEncodesPassword() {
		UUID id = UUID.randomUUID();
		User existing = new User(id, "alice", "a@b.c", "old");
		when(userRepository.findById(id)).thenReturn(Optional.of(existing));
		when(userRepository.findByUsername("alice2")).thenReturn(Optional.empty());
		when(passwordEncoder.encode("newpass")).thenReturn("enc-new");
		when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

		User updated = userService.updateUser(id, "alice2", "a2@b.c", "newpass");

		assertThat(updated.getUsername()).isEqualTo("alice2");
		assertThat(updated.getEmail()).isEqualTo("a2@b.c");
		assertThat(updated.getPassword()).isEqualTo("enc-new");
	}
}
