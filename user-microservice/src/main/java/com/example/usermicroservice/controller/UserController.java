package com.example.usermicroservice.controller;

import com.example.usermicroservice.config.security.JWTUtil;
import com.example.usermicroservice.config.security.PBKDF2Encoder;
import com.example.usermicroservice.dto.UserDto;
import com.example.usermicroservice.entity.Role;
import com.example.usermicroservice.entity.UserLogin;
import com.example.usermicroservice.entity.authentication.AuthRequest;
import com.example.usermicroservice.entity.authentication.AuthResponse;
import com.example.usermicroservice.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@Tag(name = "User Api", description = "Api for testing the endpoint for Users")
@RequestMapping("/users")
@RequiredArgsConstructor
@Validated
public class UserController {

    private final JWTUtil jwtUtil;

    private final PBKDF2Encoder passwordEncoder;

    private final UserService userService;

    @PostMapping("/{role}")
    public ResponseEntity<Mono<UserDto>> saveUser(@Valid @RequestBody UserDto user, @PathVariable Role role) {
        return ResponseEntity.ok(userService.save(user, role));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Mono<UserDto>> getUser(@PathVariable Long id) {
        return ResponseEntity.ok(userService.findById(id));
    }

    @PostMapping("/login")
    public Mono<ResponseEntity<AuthResponse>> login(@RequestBody AuthRequest ar) {
        return userService.findByUsername(ar.getUsername())
                .filter(userDetails -> passwordEncoder.encode(ar.getPassword()).equals(userDetails.getPassword()))
                .map(userDetails -> ResponseEntity.ok(new AuthResponse(jwtUtil.generateToken(userDetails), userDetails.getId())))
                .switchIfEmpty(Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()));
    }
}
