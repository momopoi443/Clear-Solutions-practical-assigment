package com.example.clearsolutionspracticalassigment.repository;

import com.example.clearsolutionspracticalassigment.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUuid(UUID uuid);

    Optional<User> findByEmail(String email);

    List<User> findAllByBirthDateBetween(LocalDate birthDateFrom, LocalDate birthDateTo);

    boolean existsByUuid(UUID uuid);

    void deleteByUuid(UUID uuid);
}
