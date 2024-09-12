package ru.kata.spring.boot_security.demo.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;

public interface UserService extends UserDetailsService {
    List<User> getAllUser();
    User getUserById(Long id);
    void saveUser(User user,List<Long>roles);
    void deleteUser(Long id);
    void updateUser(User user, Long id,List<Long>roles);
    User findByUsername(String username);
}