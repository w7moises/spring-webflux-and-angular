package com.example.usermicroservice.service;

import com.example.usermicroservice.dto.UserDto;
import com.example.usermicroservice.entity.Role;
import com.example.usermicroservice.exception.UserFoundException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient(timeout = "20000")
public class UserServiceTest {
    @Autowired
    private WebTestClient webClient;
    @MockBean
    private UserService userService;
    private static UserDto user = new UserDto();
    private final static String firstname = "John";
    private final static String lastname = "Doe";
    private final static String username = "johndoe";
    private final static String email = "email@gmail.com";
    private final static String password = "password";
    private final static Role role = Role.ROLE_USER;

    @BeforeAll
    public static void setUp() {
        user.setFirstname(firstname);
        user.setLastname(lastname);
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);
    }

    @Test
    @DisplayName("Test Save User")
    public void testSaveUser() {
        when(userService.save(user, role)).thenReturn(Mono.just(user));
        webClient.post().uri("/users/{role}", role)
                .bodyValue(user)
                .exchange()
                .expectStatus().isOk()
                .expectBody(UserDto.class)
                .isEqualTo(user);
    }

    @Test
    @DisplayName("Test Save User Who Already Exists")
    public void testSaveUserWhoAlreadyExists() {
        when(userService.save(user, role)).thenReturn(Mono.error(new UserFoundException("El usuario ya existe: " + user.getUsername())));
        webClient.post().uri("/users/{role}", role)
                .bodyValue(user)
                .exchange()
                .expectStatus().isFound();
    }
}
