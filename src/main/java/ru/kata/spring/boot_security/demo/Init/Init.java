package ru.kata.spring.boot_security.demo.Init;

import java.util.HashSet;
import java.util.Set;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.entities.Role;
import ru.kata.spring.boot_security.demo.entities.User;
import ru.kata.spring.boot_security.demo.services.RoleService;
import ru.kata.spring.boot_security.demo.services.UserService;

@Component
public class Init {
    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public Init(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @PostConstruct
    private void initializedDataBase() {
        Role roleAdmin = new Role("ROLE_ADMIN");
        Role roleUser = new Role("ROLE_USER");
        roleService.saveRole(roleAdmin);
        roleService.saveRole(roleUser);
        Set<Role> adminRoleSet = new HashSet<>();
        Set<Role> userRoleSet = new HashSet<>();
        System.out.println(adminRoleSet.add(roleAdmin));
        System.out.println(adminRoleSet.add(roleUser));
        System.out.println(userRoleSet.add(roleUser));
        User admin = new User(
                "adminchik", "100", "admin@kata.ru", "Администратор", "Админский");
        User admin2 = new User("admin", "admin", "admin@kata.ru",
                "АдминистраторВсеяАдминов", "Админский");
        User user1 =
                new User("user", "user", "user@kata.ru", "Пользователь", "Такойто");
        User user2 = new User(
                "Пользовалка", "100", "userka@kata.ru", "Пользовалка", "Такаята");
        admin.setRoles(adminRoleSet);
        admin2.setRoles(adminRoleSet);
        user1.setRoles(userRoleSet);
        user2.setRoles(userRoleSet);
        userService.addUser(admin);
        userService.addUser(admin2);
        userService.addUser(user1);
        userService.addUser(user2);
    }
}
