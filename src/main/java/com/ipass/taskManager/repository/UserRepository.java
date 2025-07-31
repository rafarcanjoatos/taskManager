package com.ipass.taskManager.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ipass.taskManager.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
}