package ru.kata.spring.boot_security.demo.services;

import java.util.List;
import ru.kata.spring.boot_security.demo.entities.Role;

public interface RoleService {
    List<Role> getListRoles();
    void saveRole(Role role);
}
