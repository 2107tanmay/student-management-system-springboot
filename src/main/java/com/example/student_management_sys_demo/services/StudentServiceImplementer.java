package com.example.student_management_sys_demo.services;

import com.example.student_management_sys_demo.model.Student;
import com.example.student_management_sys_demo.repository.StudentRepository;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.student_management_sys_demo.exceptions.DuplicateResourceException;
import com.example.student_management_sys_demo.exceptions.ResourceNotFoundException;

import java.time.LocalDate;
import java.util.List;

@Service
//@Service is used to signify this is a service class to spring bean factory
@Transactional
//this annotation in used to specify that an interface/class/method must be transactional semantics
//eg: start a brand new transaction everytime
public class StudentServiceImplementer implements StudentService {

    private final StudentRepository studentRepository;

    // Constructor-based dependency injection (recommended over @Autowired on field)
    @Autowired
    public StudentServiceImplementer(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public Student createStudent(Student student) {
        // Business logic: Set enrollment date if not provided
        if (student.getEnrollmentDate() == null) {
            student.setEnrollmentDate(LocalDate.now());
        }

        // Validate unique email
        if (studentRepository.existsByEmail(student.getEmail())) {
            throw new DuplicateResourceException("Email already exists: " + student.getEmail());
        }

        // Save using JPA repository
        return studentRepository.save(student);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Student> getStudentById(Long id) {
        return studentRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Student> searchStudents(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return studentRepository.findAll();
        }
        return studentRepository.searchStudents(keyword);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Student> getStudentsByDepartment(String department) {
        return studentRepository.findByDepartmentSorted(department);
    }

    @Override
    public Student updateStudent(Long id, Student studentDetails) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + id));

        // Check if email is being changed and if new email already exists
        if (!student.getEmail().equals(studentDetails.getEmail())
                && studentRepository.existsByEmail(studentDetails.getEmail())) {
            throw new DuplicateResourceException("Email already exists: " + studentDetails.getEmail());
        }

        // Update fields
        student.setFirstName(studentDetails.getFirstName());
        student.setLastName(studentDetails.getLastName());
        student.setEmail(studentDetails.getEmail());
        student.setDateOfBirth(studentDetails.getDateOfBirth());
        student.setPhoneNumber(studentDetails.getPhoneNumber());
        student.setDepartment(studentDetails.getDepartment());

        // JPA automatically detects changes and updates the entity
        return studentRepository.save(student);
    }

    @Override
    public void deleteStudent(Long id) {
        if (!studentRepository.existsById(id)) {
            throw new ResourceNotFoundException("Student not found with id: " + id);
        }
        studentRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean emailExists(String email) {
        return studentRepository.existsByEmail(email);
    }

    @Override
    @Transactional(readOnly = true)
    public long getStudentCountByDepartment(String department) {
        return studentRepository.countByDepartment(department);
    }

}