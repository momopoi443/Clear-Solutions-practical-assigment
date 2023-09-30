package com.example.clearsolutionspracticalassigment.repository;

import com.example.clearsolutionspracticalassigment.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUuid(UUID uuid);
}
