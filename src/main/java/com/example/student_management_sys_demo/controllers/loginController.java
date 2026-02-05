package com.example.student_management_sys_demo.controllers;

import com.example.student_management_sys_demo.dto.RegistrationForm;
import com.example.student_management_sys_demo.model.Role;
import com.example.student_management_sys_demo.model.User;
import com.example.student_management_sys_demo.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.validation.BindingResult;
@Controller
public class loginController {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public loginController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("registrationForm", new RegistrationForm());
        return "register";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("registrationForm") RegistrationForm form,
                           BindingResult bindingResult) {
        if (!bindingResult.hasFieldErrors("confirmPassword")
                && !form.getPassword().equals(form.getConfirmPassword())) {
            bindingResult.rejectValue("confirmPassword", "confirmPassword.mismatch", "Passwords do not match.");
        }

        if (!bindingResult.hasFieldErrors("username")
                && userRepository.existsByUsername(form.getUsername())) {
            bindingResult.rejectValue("username", "username.exists", "Username is already taken.");
        }

        if (bindingResult.hasErrors()) {
            return "register";
        }

        User user = new User();
        user.setUsername(form.getUsername());
        user.setPassword(passwordEncoder.encode(form.getPassword()));
        user.setRole(Role.STUDENT);
        userRepository.save(user);

        return "redirect:/login?registered";
    }

    @GetMapping("/")
    public String root() {
        return "redirect:/login";
    }
}