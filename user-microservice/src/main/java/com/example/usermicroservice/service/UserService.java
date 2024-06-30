package com.example.usermicroservice.service;

import com.example.usermicroservice.dto.UserDto;
import com.example.usermicroservice.entity.Role;
import com.example.usermicroservice.entity.UserLogin;
import reactor.core.publisher.Mono;

public interface UserService {
    Mono<UserLogin> findByUsername(String username);

    Mono<UserDto> findById(Long id);

    Mono<UserDto> save(UserDto user, Role role);
}
