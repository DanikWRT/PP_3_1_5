//package ru.kata.spring.boot_security.demo.controllers;
//
//import javax.validation.Valid;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.annotation.AuthenticationPrincipal;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.validation.BindingResult;
//import org.springframework.web.bind.annotation.*;
//import ru.kata.spring.boot_security.demo.entities.User;
//import ru.kata.spring.boot_security.demo.services.RoleService;
//import ru.kata.spring.boot_security.demo.services.UserService;
//
//@Controller
//@RequestMapping("/admin")
//public class AdminController {
//    private final UserService userService;
//    private final RoleService roleService;
//
//    @Autowired
//    public AdminController(UserService userService, RoleService roleService) {
//        this.userService = userService;
//        this.roleService = roleService;
//    }
//
//    @GetMapping("/user")
//    public String getUsers(@AuthenticationPrincipal UserDetails userDetails, Model model
//            , @ModelAttribute("newUser") User newUser) {
//        User currentUser = userService.getUserByName(userDetails.getUsername());
//        model.addAttribute("users", userService.getUsers());
//        model.addAttribute("currentUser", currentUser);
//        model.addAttribute("listRoles", roleService.getListRoles());
//        model.addAttribute("newUser", newUser);
//        return "users";
//    }
//
//    @PostMapping("/user")
//    public String createUser(
//            @ModelAttribute("user") @Valid User user, BindingResult result) {
//        if (result.hasErrors()) {
//            return "users";
//        }
//        userService.addUser(user);
//        return "redirect:/admin/user";
//    }
//
//
//    @PostMapping("/user/{id}")
//    public String editUser(@ModelAttribute("user") @Valid User user,
//                           @PathVariable("id") Long id, BindingResult result) {
//        if (result.hasErrors()) {
//            return "users";
//        }
//        userService.editUser(id, user);
//        return "redirect:/admin/user";
//    }
//
//    @GetMapping("/delete/{id}")
//    public String deleteUser(@PathVariable("id") Long id) {
//        this.userService.deleteUser(id);
//        return "redirect:/admin/user";
//    }
//}