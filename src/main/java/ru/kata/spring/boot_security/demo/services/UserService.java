package ru.kata.spring.boot_security.demo.services;

import ru.kata.spring.boot_security.demo.etities.User;

import java.util.List;

public interface UserService {
    User getUserByName(String name);
    List<User> getUsers();
    void addUser(User user);
    void deleteUser(User user);
    void editUser(User user);
    User getUserById(Long id);
}
