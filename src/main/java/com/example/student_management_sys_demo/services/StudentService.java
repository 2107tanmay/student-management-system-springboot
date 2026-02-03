package com.example.student_management_sys_demo.services;


import com.example.student_management_sys_demo.model.Student;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface StudentService {
    // Create
    Student createStudent(Student student);

    // Read
    List<Student> getAllStudents();
    Optional<Student> getStudentById(Long id);
    List<Student> searchStudents(String keyword);
    List<Student> getStudentsByDepartment(String department);

    // Update
    Student updateStudent(Long id, Student student);

    // Delete
    void deleteStudent(Long id);

    // Additional business logic
    boolean emailExists(String email);
    long getStudentCountByDepartment(String department);
}
