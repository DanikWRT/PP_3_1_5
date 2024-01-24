package ru.kata.spring.boot_security.demo.services;

import java.util.List;
import ru.kata.spring.boot_security.demo.entities.User;

public interface UserService {
    User getUserByName(String name);

    List<User> getUsers();

    void addUser(User user);

    void deleteUser(Long id);

    void editUser(Long id, User user);

    User getUserById(Long id);
}
