package ru.kata.spring.boot_security.demo.services;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import ru.kata.spring.boot_security.demo.etities.User;
import ru.kata.spring.boot_security.demo.repositories.UserRepository;

import javax.transaction.Transactional;
import java.util.List;

public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public User getUserByName(String name) {
        return userRepository.findByUsername(name);
    }

    @Override
    public List<User> getUsers() {
        return userRepository.findAll();
    }
    @Transactional
    @Override
    public void addUser(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    @Override
    public void deleteUser(User user) {
        userRepository.delete(user);
    }
    @Transactional
    @Override
    public void editUser(User user) {
        User existingUser = userRepository.findById(user.getId()).orElse(null);
        if (existingUser != null) {
            existingUser.setUsername(user.getUsername());
            existingUser.setEmail(user.getEmail());
            existingUser.setRoles(user.getRoles());
            if(!existingUser.getPassword().equals(user.getPassword())) {
                existingUser.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            }
        }
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }
}
