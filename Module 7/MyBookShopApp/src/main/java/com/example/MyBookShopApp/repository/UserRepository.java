package com.example.MyBookShopApp.repository;

import com.example.MyBookShopApp.model.Book;
import com.example.MyBookShopApp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findUserById(Integer id);
}
