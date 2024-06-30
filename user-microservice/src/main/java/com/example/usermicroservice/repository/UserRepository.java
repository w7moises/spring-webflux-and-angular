package com.example.usermicroservice.repository;

import com.example.usermicroservice.entity.UserLogin;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface UserRepository extends ReactiveCrudRepository<UserLogin, Long> {
    Mono<UserLogin> findByUsername(String username);
}
