package com.example.student_management_sys_demo.repository;

import com.example.student_management_sys_demo.model.Teacher;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {
    Optional<Teacher> findByUserId(Long userId);
}
