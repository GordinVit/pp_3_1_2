package ru.kata.spring.boot_security.demo.configs;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.RoleRepository;
import ru.kata.spring.boot_security.demo.service.UserServiceImpl;

@Configuration
public class DataInitializer {
    @Bean
    public CommandLineRunner init(UserServiceImpl userService, RoleRepository roleRepository) {
        return args -> {
            Role userRole = new Role();
            userRole.setName("ROLE_USER");
            roleRepository.save(userRole);

            Role adminRole = new Role();
            adminRole.setName("ROLE_ADMIN");
            roleRepository.save(adminRole);

            User user = new User();
            user.setUsername("user@mail.ru");
            user.setPassword("user@mail.ru");
            user.setEmail("user@mail.ru");
            userService.saveUser(user, List.of(userRole.getId()));

            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword("admin123");
            admin.setEmail("admin@mail.ru");
            userService.saveUser(admin, List.of(adminRole.getId()));
        };
    }
}