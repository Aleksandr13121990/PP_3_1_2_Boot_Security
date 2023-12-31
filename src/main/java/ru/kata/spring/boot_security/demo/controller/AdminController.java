package ru.kata.spring.boot_security.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repositories.RoleRepository;
import ru.kata.spring.boot_security.demo.service.UserService;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;


@Controller
@RequestMapping()
public class AdminController {

    private final UserService userService;

    private final RoleRepository roleRepository;

    public AdminController(UserService userService, RoleRepository roleRepository) {
        this.userService = userService;
        this.roleRepository = roleRepository;
    }

    @GetMapping("/")
    public String registration() {
        return "admin/registration";
    }

    @GetMapping("/admin")
    public String listUsers(ModelMap modelMap) {
        modelMap.addAttribute("users", userService.allUsers());
        return "admin/admin";
    }

    @GetMapping("/user")
    public String showUser(@RequestParam("username") String username, Principal principal, ModelMap modelMap) {
        User user = userService.findByUsername(username);
        modelMap.addAttribute("user", user);
        return "admin/user";
    }

    @GetMapping("/new")
    public String newUser(@ModelAttribute("user") User user, ModelMap modelMap) {
        List<Role> roles = roleRepository.findAll();
        modelMap.addAttribute("user", user);
        modelMap.addAttribute("allRoles", roles);
        return "admin/new-user";
    }

    @PostMapping()
    public String createUser(@ModelAttribute("user") @Valid User user, BindingResult bindingResult,
                             RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("user", user);
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            return "redirect:/new";
        }
        if (!userService.saveUser(user)) {
            redirectAttributes.addFlashAttribute("user", user);
            redirectAttributes.addFlashAttribute("usernameErrors", "Пользователь с таким именем уже существует");
            return "redirect:/new";
        }
        userService.saveUser(user);
        return "redirect:/admin";
    }

    @GetMapping("/edit")
    public String editUser(@RequestParam("id") long id, ModelMap modelMap) {
        List<Role> roles = roleRepository.findAll();
        modelMap.addAttribute("user", userService.getUserById(id));
        modelMap.addAttribute("allRoles", roles);
        return "admin/edit-user";
    }

    @PostMapping("/update")
    public String updateUser(@ModelAttribute("user") @Valid User user, @RequestParam("id") long id) {
        userService.updateUser(user);
        return "redirect:/admin";
    }

    @PostMapping("/delete")
    public String deleteUser(@ModelAttribute("user") User user, @RequestParam("id") long id) {
        userService.deleteUser(id);
        return "redirect:/admin";
    }
}
