package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.RoleServiceImpl;
import ru.kata.spring.boot_security.demo.service.UserService;

import javax.validation.Valid;
import java.util.Arrays;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public AdminController(UserService userServiceImpl, RoleServiceImpl roleService) {
        this.userService = userServiceImpl;
        this.roleService = roleService;
    }

    @GetMapping()
    public String getAllUser(Model model) {
        model.addAttribute("users", userService.getAllUser());
        model.addAttribute("allRoles", roleService.getAllRoles());
        model.addAttribute("newUser", new User());
        return "users";
    }

    @GetMapping("/{id}")
    public String getUser(@RequestParam("id") Long id, Model model) {
        model.addAttribute("user", userService.getUserById(id));
        return "show";
    }

    @PostMapping("/new")
    public String createUser(@ModelAttribute("user") @Valid User user,
                             @RequestParam(name = "selectedRoles", required = false) Long[] selectedRoles) {
        userService.saveUser(user, Arrays.asList(selectedRoles));
        return "redirect:/admin";
    }


    @GetMapping("/edit")
    public String editUserForm(Model model, @RequestParam("id") Long id) {
        model.addAttribute("user", userService.getUserById(id));
        model.addAttribute("allRoles", roleService.getAllRoles());
        return "edit";
    }

    @PostMapping("/edit")
    public String updateUser(@ModelAttribute("user") @Valid User user,
                             @RequestParam("id") Long id, @RequestParam(name = "selectedRoles", required = false) Long[] selectedRoles) {
        userService.updateUser(user, id, Arrays.asList(selectedRoles));
        return "redirect:/admin";
    }


    @PostMapping("/delete")
    public String deleteUser(@RequestParam("id") Long id) {
        userService.deleteUser(id);
        return "redirect:/admin";
    }
}