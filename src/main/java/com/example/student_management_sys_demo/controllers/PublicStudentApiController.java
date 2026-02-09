package com.example.student_management_sys_demo.controllers;

import com.example.student_management_sys_demo.dto.StudentSummary;
import com.example.student_management_sys_demo.model.Student;
import com.example.student_management_sys_demo.services.StudentService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/public")
public class PublicStudentApiController {

    private final StudentService studentService;

    public PublicStudentApiController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/students")
    public List<StudentSummary> getStudents() {
        return studentService.getAllStudents()
                .stream()
                .map(this::toSummary)
                .toList();
    }

    private StudentSummary toSummary(Student student) {
        return new StudentSummary(
                student.getId(),
                student.getFirstName(),
                student.getLastName(),
                student.getEmail(),
                student.getDepartment(),
                student.getEnrollmentDate()
        );
    }
}
