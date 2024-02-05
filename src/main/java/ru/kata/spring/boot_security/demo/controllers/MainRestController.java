package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.entities.User;
import ru.kata.spring.boot_security.demo.entities.Role;
import ru.kata.spring.boot_security.demo.services.RoleService;
import ru.kata.spring.boot_security.demo.services.UserService;
import ru.kata.spring.boot_security.demo.util.UserNotCreatedException;
import ru.kata.spring.boot_security.demo.util.UserNotFoundException;
import ru.kata.spring.boot_security.demo.util.UsersErrorResponse;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class MainRestController {
    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public MainRestController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        return new ResponseEntity<>(userService.getUsers(), HttpStatus.OK);
    }

    @GetMapping("/roles")
    public ResponseEntity<List<Role>> getRole() {
        return new ResponseEntity<>(roleService.getListRoles(), HttpStatus.OK);
    }

    @GetMapping("/info")
    public ResponseEntity<User> getPrincipal(Principal principal) {
        return new ResponseEntity<>(userService.getUserByName(principal.getName()), HttpStatus.OK);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<User> getOneUser(@PathVariable("id") Long id) {
        //exeption перехватывает @ExceptionHandler если ловит HTTPStatus.BAD_REQUEST
        return new ResponseEntity<>(userService.getUserById(id), HttpStatus.OK);
        //Jackson конвертирует в JSON
    }

    @PostMapping("/create")
    public ResponseEntity<User> creatRestUser(@RequestBody @Valid User user,
                                              BindingResult bindingResult) {
        errorMessageBildering(user, bindingResult);
        userService.addUser(user);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    private void errorMessageBildering(@RequestBody @Valid User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            StringBuilder errorMsg = new StringBuilder();
            List<FieldError> errors = bindingResult.getFieldErrors();
            for (FieldError error : errors) {
                errorMsg.append(error.getField())
                        .append(" - ").append(error.getDefaultMessage())
                        .append(";");
            }
            throw new UserNotCreatedException(errorMsg.toString());
        }
        List<String> list1 = user.getRoles().stream().map(Role::getName).collect(Collectors.toList());
        List<Role> list2 = roleService.listByRole(list1);
        user.setRoles(Set.copyOf(list2));
    }

    @PatchMapping("/update")
    public ResponseEntity<User> updateRestUser(@RequestBody User user
            , BindingResult bindingResult) {
        errorMessageBildering(user, bindingResult);
        userService.editUser(user.getId(), user);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<HttpStatus> deleteRestUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @ExceptionHandler
    private ResponseEntity<UsersErrorResponse> handleException(UserNotFoundException e) {
        UsersErrorResponse response = new UsersErrorResponse(
                "User with this id wasn't found!",
                System.currentTimeMillis()
        );
        // В HTTP ответе тело ответа (response) и статус в заголовке
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    private ResponseEntity<UsersErrorResponse> handleException(UserNotCreatedException e) {
        UsersErrorResponse response = new UsersErrorResponse(
                e.getMessage(),
                System.currentTimeMillis()
        );
        // В HTTP ответе тело ответа (response) и статус в заголовке
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
