package com.example.student_management_sys_demo.repository;

import com.example.student_management_sys_demo.model.Enrollment;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    long countByCourseId(Long courseId);
    boolean existsByStudentIdAndCourseId(Long studentId, Long courseId);
    List<Enrollment> findByStudentId(Long studentId);
}