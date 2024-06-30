package com.example.usermicroservice.service.impl;

import com.example.usermicroservice.config.security.PBKDF2Encoder;
import com.example.usermicroservice.dto.UserDto;
import com.example.usermicroservice.entity.Role;
import com.example.usermicroservice.entity.UserLogin;
import com.example.usermicroservice.exception.UserFoundException;
import com.example.usermicroservice.repository.UserRepository;
import com.example.usermicroservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final ModelMapper modelMapper;

    private final PBKDF2Encoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    public Mono<UserLogin> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public Mono<UserDto> findById(Long id) {
        return userRepository.findById(id)
                .map(user -> modelMapper.map(user, UserDto.class))
                .switchIfEmpty(Mono.error(new UserFoundException("Usuario no encontrado con id: " + id)));
    }

    @Override
    @Transactional
    public Mono<UserDto> save(UserDto user, Role role) {
        UserLogin userOptional = modelMapper.map(user, UserLogin.class);

        return userRepository.findByUsername(user.getUsername())
                .flatMap(existingUser -> Mono.error(new UserFoundException("El usuario ya existe: " + user.getUsername())))
                .switchIfEmpty(Mono.defer(() -> {
                    userOptional.setEnabled(true);
                    userOptional.setRole(role);
                    userOptional.setPassword(passwordEncoder.encode(user.getPassword()));
                    return userRepository.save(userOptional);
                })).map(savedUser -> modelMapper.map(savedUser, UserDto.class));
    }
}
