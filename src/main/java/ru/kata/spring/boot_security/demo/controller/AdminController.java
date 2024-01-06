package ru.kata.spring.boot_security.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import javax.validation.Valid;
import java.util.List;


@Controller
@RequestMapping()
public class AdminController {

    private final UserService userService;

    private final RoleService roleService;

    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping("/admin")
    public String listUsers(ModelMap modelMap) {
        modelMap.addAttribute("users", userService.allUsers());
        return "admin/admin";
    }

    @GetMapping("/admin/new")
    public String newUser(@ModelAttribute("user") User user, ModelMap modelMap) {
        List<Role> roles = roleService.allRoles();
        modelMap.addAttribute("user", user);
        modelMap.addAttribute("allRoles", roles);
        return "admin/new-user";
    }

    @PostMapping("/admin")
    public String createUser(@ModelAttribute("user") @Valid User user, BindingResult bindingResult,
                             RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("user", user);
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            return "redirect:/admin/new";
        }
        if (!userService.saveUser(user)) {
            redirectAttributes.addFlashAttribute("user", user);
            redirectAttributes.addFlashAttribute("usernameErrors", "Пользователь с таким именем уже существует");
            return "redirect:/admin/new";
        }
        userService.saveUser(user);
        return "redirect:/admin";
    }

    @GetMapping("/admin/edit")
    public String editUser(@ModelAttribute("user") User user, @RequestParam("id") long id, ModelMap modelMap) {
        List<Role> roles = roleService.allRoles();
        modelMap.addAttribute("user", userService.getUserById(id));
        modelMap.addAttribute("allRoles", roles);
        return "admin/edit-user";
    }

    @PostMapping("/admin/update")
    public String updateUser(@ModelAttribute("user") @Valid User user, BindingResult bindingResult,
                             RedirectAttributes redirectAttributes, @RequestParam("id") long id) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("user", user);
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            return "redirect:/admin/edit?id=" + id;
        }
        if (!userService.saveUser(user)) {
            User userDB = userService.findByUsername(user.getUsername());
            if (id == userDB.getId()) {
                userService.saveUser(user);
            } else {
                redirectAttributes.addFlashAttribute("user", user);
                redirectAttributes.addFlashAttribute("usernameErrors", "Пользователь с таким именем уже существует");
                return "redirect:/admin/edit?id=" + id;
            }
        }
        userService.updateUser(user);
        return "redirect:/admin";
    }

    @PostMapping("/admin/delete")
    public String deleteUser(@ModelAttribute("user") User user, @RequestParam("id") long id) {
        userService.deleteUser(id);
        return "redirect:/admin";
    }
}
