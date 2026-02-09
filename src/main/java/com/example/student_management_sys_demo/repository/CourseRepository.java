package com.example.student_management_sys_demo.repository;

import com.example.student_management_sys_demo.model.Course;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Long> {
    List<Course> findByTeacherId(Long teacherId);
}