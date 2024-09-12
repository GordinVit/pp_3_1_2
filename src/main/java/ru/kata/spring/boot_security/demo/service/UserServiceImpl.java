package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.RoleRepository;
import ru.kata.spring.boot_security.demo.repository.UserRepository;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    @Override
    public List<User> getAllUser() {
        return userRepository.findAll();
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.getById(id);
    }

    @Override
    @Transactional
    public void saveUser(User user, List<Long> roles) {
        if (user == null || StringUtils.isEmpty(user.getUsername()) || StringUtils.isEmpty(user.getEmail())
                || StringUtils.isEmpty(user.getPassword()) || CollectionUtils.isEmpty(roles)) {
            throw new IllegalArgumentException("Invalid user data");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        List<Role> roles1 = roleRepository.findAllById(roles);
        user.setRoles(new HashSet<>(roles1));
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void updateUser(User user, Long id, List<Long> selectedRoles) {
        User savedUser = userRepository.getById(id);
        if (savedUser == null) {
            throw new IllegalArgumentException("User with id " + id + " not found");
        }
        if (user == null || StringUtils.isEmpty(user.getUsername()) || StringUtils.isEmpty(user.getEmail())
                || StringUtils.isEmpty(user.getPassword()) || CollectionUtils.isEmpty(selectedRoles)) {
            throw new IllegalArgumentException("Invalid user data");
        }
        savedUser.setUsername(user.getUsername());
        savedUser.setEmail(user.getEmail());
        savedUser.setPassword(passwordEncoder.encode(user.getPassword()));
        List<Role> roles = roleRepository.findAllById(selectedRoles);
        savedUser.setRoles(new HashSet<>(roles));
        userRepository.save(savedUser);
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("No user found with username" + username);
        }
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                mapRolesToAuthorities(user.getRoles()));
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
        return roles.stream().map(r -> new SimpleGrantedAuthority(r.getName())).collect(Collectors.toList());

    }
}