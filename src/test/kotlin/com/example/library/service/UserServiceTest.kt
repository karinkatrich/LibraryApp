package com.example.library.service

import com.example.library.model.User
import com.example.library.repo.UserRepository
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.springframework.test.context.ActiveProfiles
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@RunWith(MockitoJUnitRunner::class)
@ActiveProfiles("dev")
class UserServiceTest {

    @Mock
    lateinit var userRepository: UserRepository;

    lateinit var userService: UserService;

    @Before
    fun setup() {
        this.userService = UserService(userRepository);
    }

    @Test
    fun `Saving a User - Happy Path`() {
        // Given
        var user : User = getUser();
        `when`(userRepository.save(user)).thenReturn(Mono.just(user));

        // When
        var actual = userService.save(user);

        // Then
        actual.subscribe({u -> assertThat(user).isEqualTo(u)});
        verify(userRepository, times(1)).save(user);
    }

    @Test
    fun `Find by Username - Happy Path`() {
        // Given
        var user : User = getUser();
        `when`(userRepository.findByUsername("shazin")).thenReturn(Flux.just(user));

        // When
        var actual = userService.getByUsername("shazin");

        // Then
        actual.subscribe({u -> assertThat(user).isEqualTo(u)});
        verify(userRepository, times(1)).findByUsername("shazin");
    }

    fun getUser() : User {
        return User(1, "shazin", "password", "USER", "User of Book");
    }

}