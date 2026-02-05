package com.example.student_management_sys_demo.config;

import com.example.student_management_sys_demo.model.Role;
import com.example.student_management_sys_demo.model.Student;
import com.example.student_management_sys_demo.model.StudentProfile;
import com.example.student_management_sys_demo.model.User;
import com.example.student_management_sys_demo.repository.StudentRepository;
import com.example.student_management_sys_demo.repository.UserRepository;
import java.time.LocalDate;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner seedData(StudentRepository studentRepository,
                                      UserRepository userRepository,
                                      PasswordEncoder passwordEncoder) {
        return args -> {
//            if (!userRepository.existsByUsername("admin")) {
//                User admin = new User();
//                admin.setUsername("admin");
//                admin.setPassword(passwordEncoder.encode("admin123"));
//                admin.setRole(Role.ADMIN);
//                userRepository.save(admin);
//            }
//
//            if (!userRepository.existsByUsername("staff")) {
//                User staff = new User();
//                staff.setUsername("staff");
//                staff.setPassword(passwordEncoder.encode("staff123"));
//                staff.setRole(Role.STAFF);
//                userRepository.save(staff);
//            }

            ensureSeedUser(userRepository, passwordEncoder, "admin", "admin123", Role.ADMIN);
            ensureSeedUser(userRepository, passwordEncoder, "staff", "staff123", Role.STAFF);

            if (studentRepository.count() == 0) {
                Student alice = new Student("Alice", "Johnson", "alice.johnson@example.com");
                alice.setDepartment("Computer Science");
                alice.setPhoneNumber("555-0101");
                alice.setDateOfBirth(LocalDate.of(2001, 3, 15));
                StudentProfile aliceProfile = new StudentProfile();
                aliceProfile.setAddressLine("123 Maple Street");
                aliceProfile.setCity("Springfield");
                aliceProfile.setGuardianName("Mark Johnson");
                aliceProfile.setEmergencyPhone("555-1001");
                alice.setProfile(aliceProfile);

                Student brian = new Student("Brian", "Lee", "brian.lee@example.com");
                brian.setDepartment("Business Administration");
                brian.setPhoneNumber("555-0102");
                brian.setDateOfBirth(LocalDate.of(2000, 8, 9));
                StudentProfile brianProfile = new StudentProfile();
                brianProfile.setAddressLine("45 Oak Avenue");
                brianProfile.setCity("Riverside");
                brianProfile.setGuardianName("Angela Lee");
                brianProfile.setEmergencyPhone("555-1002");
                brian.setProfile(brianProfile);

                Student cynthia = new Student("Cynthia", "Patel", "cynthia.patel@example.com");
                cynthia.setDepartment("Mechanical Engineering");
                cynthia.setPhoneNumber("555-0103");
                cynthia.setDateOfBirth(LocalDate.of(2002, 11, 22));

                studentRepository.save(alice);
                studentRepository.save(brian);
                studentRepository.save(cynthia);
            }
        };
    }

    private void ensureSeedUser(UserRepository userRepository,
                                PasswordEncoder passwordEncoder,
                                String username,
                                String rawPassword,
                                Role role) {
        User user = userRepository.findByUsername(username).orElseGet(User::new);
        boolean needsSave = user.getId() == null;

        if (needsSave) {
            user.setUsername(username);
        }

        if (user.getRole() == null || user.getRole() != role) {
            user.setRole(role);
            needsSave = true;
        }

        if (user.getPassword() == null || !isBcryptHash(user.getPassword())) {
            user.setPassword(passwordEncoder.encode(rawPassword));
            needsSave = true;
        }

        if (needsSave) {
            userRepository.save(user);
        }
    }

    private boolean isBcryptHash(String password) {
        return password.startsWith("$2a$") || password.startsWith("$2b$") || password.startsWith("$2y$");
    }
}
