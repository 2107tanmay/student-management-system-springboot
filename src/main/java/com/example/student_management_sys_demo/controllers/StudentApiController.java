package com.example.student_management_sys_demo.controllers;

import com.example.student_management_sys_demo.dto.ChangeRequestDto;
import com.example.student_management_sys_demo.dto.ProfileImageUpdateRequest;
import com.example.student_management_sys_demo.model.Attendance;
import com.example.student_management_sys_demo.model.ChangeRequest;
import com.example.student_management_sys_demo.model.Enrollment;
import com.example.student_management_sys_demo.model.Grade;
import com.example.student_management_sys_demo.model.User;
import com.example.student_management_sys_demo.repository.UserRepository;
import com.example.student_management_sys_demo.services.AttendanceService;
import com.example.student_management_sys_demo.services.ChangeRequestService;
import com.example.student_management_sys_demo.services.EnrollmentService;
import com.example.student_management_sys_demo.services.GradeService;
import java.security.Principal;
import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/student")
public class StudentApiController {

    private final EnrollmentService enrollmentService;
    private final GradeService gradeService;
    private final AttendanceService attendanceService;
    private final ChangeRequestService changeRequestService;
    private final UserRepository userRepository;

    public StudentApiController(EnrollmentService enrollmentService,
                                GradeService gradeService,
                                AttendanceService attendanceService,
                                ChangeRequestService changeRequestService,
                                UserRepository userRepository) {
        this.enrollmentService = enrollmentService;
        this.gradeService = gradeService;
        this.attendanceService = attendanceService;
        this.changeRequestService = changeRequestService;
        this.userRepository = userRepository;
    }

    @PostMapping("/courses/{courseId}/enroll")
    @ResponseStatus(HttpStatus.CREATED)
    public Enrollment enroll(@PathVariable Long courseId, Principal principal) {
        Long studentUserId = resolveUserId(principal);
        return enrollmentService.enrollStudent(studentUserId, courseId);
    }

    @GetMapping("/enrollments")
    public List<Enrollment> getEnrollments(Principal principal) {
        Long studentUserId = resolveUserId(principal);
        return enrollmentService.getStudentEnrollments(studentUserId);
    }

    @GetMapping("/grades")
    public List<Grade> getGrades(Principal principal) {
        Long studentUserId = resolveUserId(principal);
        return gradeService.getStudentGrades(studentUserId);
    }

    @GetMapping("/attendance")
    public List<Attendance> getAttendance(Principal principal) {
        Long studentUserId = resolveUserId(principal);
        return attendanceService.getStudentAttendance(studentUserId);
    }

    @PostMapping("/change-requests")
    public ChangeRequest requestChange(@RequestBody ChangeRequestDto request, Principal principal) {
        Long studentUserId = resolveUserId(principal);
        return changeRequestService.createRequest(studentUserId, request.getFieldName(), request.getNewValue());
    }

    @PutMapping("/profile-image")
    public User updateProfileImage(@RequestBody ProfileImageUpdateRequest request, Principal principal) {
        User user = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new NoSuchElementException("User not found"));
        user.setProfileImageUrl(request.getProfileImageUrl());
        return userRepository.save(user);
    }

    private Long resolveUserId(Principal principal) {
        return userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new NoSuchElementException("User not found"))
                .getId();
    }
}
