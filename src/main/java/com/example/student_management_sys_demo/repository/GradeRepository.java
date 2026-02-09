package com.example.student_management_sys_demo.repository;

import com.example.student_management_sys_demo.model.Grade;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GradeRepository extends JpaRepository<Grade, Long> {
    List<Grade> findByStudentId(Long studentId);
}