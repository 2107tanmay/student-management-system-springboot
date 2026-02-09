package com.example.student_management_sys_demo.controllers;

import com.example.student_management_sys_demo.dto.CreateStudentRequest;
import com.example.student_management_sys_demo.dto.CreateTeacherRequest;
import com.example.student_management_sys_demo.model.ChangeRequest;
import com.example.student_management_sys_demo.model.Student;
import com.example.student_management_sys_demo.model.Teacher;
import com.example.student_management_sys_demo.services.AdminService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {
    private AdminService adminService;

    public AdminController(AdminService adminService){
        this.adminService = adminService;
    }

    @PostMapping("/students")
    @ResponseStatus(HttpStatus.CREATED)
    public Student createStudent(@RequestBody CreateStudentRequest request) {
        return adminService.createStudent(request);
    }

    @PostMapping("/teachers")
    @ResponseStatus(HttpStatus.CREATED)
    public Teacher createTeacher(@RequestBody CreateTeacherRequest request) {
        return adminService.createTeacher(request);
    }

    @DeleteMapping("/users/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Long userId) {
        adminService.deleteUser(userId);
    }

    @PostMapping("/changes/{requestId}/approve")
    public ChangeRequest approveChange(@PathVariable Long requestId) {
        return adminService.approveChangeRequest(requestId);
    }

    @PostMapping("/changes/{requestId}/reject")
    public ChangeRequest rejectChange(@PathVariable Long requestId) {
        return adminService.rejectChangeRequest(requestId);
    }

    @GetMapping("/changes")
    public Iterable<ChangeRequest> listChangeRequests() {
        return adminService.listChangeRequests();
    }
}