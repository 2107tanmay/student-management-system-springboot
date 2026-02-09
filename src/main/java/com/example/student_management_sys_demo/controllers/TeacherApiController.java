package com.example.student_management_sys_demo.controllers;

import com.example.student_management_sys_demo.dto.AttendanceRequest;
import com.example.student_management_sys_demo.dto.ChangeRequestDto;
import com.example.student_management_sys_demo.dto.CourseRequest;
import com.example.student_management_sys_demo.dto.GradeRequest;
import com.example.student_management_sys_demo.dto.ProfileImageUpdateRequest;
import com.example.student_management_sys_demo.model.Attendance;
import com.example.student_management_sys_demo.model.ChangeRequest;
import com.example.student_management_sys_demo.model.Course;
import com.example.student_management_sys_demo.model.Grade;
import com.example.student_management_sys_demo.model.User;
import com.example.student_management_sys_demo.repository.UserRepository;
import com.example.student_management_sys_demo.services.AttendanceService;
import com.example.student_management_sys_demo.services.ChangeRequestService;
import com.example.student_management_sys_demo.services.CourseService;
import com.example.student_management_sys_demo.services.GradeService;
import java.security.Principal;
import java.util.NoSuchElementException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/teacher")
public class TeacherApiController {

    private final CourseService courseService;
    private final AttendanceService attendanceService;
    private final GradeService gradeService;
    private final ChangeRequestService changeRequestService;
    private final UserRepository userRepository;

    public TeacherApiController(CourseService courseService,
                                AttendanceService attendanceService,
                                GradeService gradeService,
                                ChangeRequestService changeRequestService,
                                UserRepository userRepository) {
        this.courseService = courseService;
        this.attendanceService = attendanceService;
        this.gradeService = gradeService;
        this.changeRequestService = changeRequestService;
        this.userRepository = userRepository;
    }

    @PostMapping("/courses")
    @ResponseStatus(HttpStatus.CREATED)
    public Course createCourse(@RequestBody CourseRequest request, Principal principal) {
        Long teacherUserId = resolveUserId(principal);
        return courseService.createCourse(teacherUserId, request.getName(), request.getSubject());
    }

    @PostMapping("/attendance")
    public Attendance markAttendance(@RequestBody AttendanceRequest request, Principal principal) {
        Long teacherUserId = resolveUserId(principal);
        return attendanceService.markAttendance(teacherUserId,
                request.getStudentId(),
                request.getCourseId(),
                request.getDate(),
                request.getStatus());
    }

    @PostMapping("/grades")
    public Grade gradeStudent(@RequestBody GradeRequest request, Principal principal) {
        Long teacherUserId = resolveUserId(principal);
        return gradeService.gradeStudent(teacherUserId,
                request.getStudentId(),
                request.getCourseId(),
                request.getScore(),
                request.getRemarks());
    }

    @PostMapping("/change-requests")
    public ChangeRequest requestChange(@RequestBody ChangeRequestDto request, Principal principal) {
        Long teacherUserId = resolveUserId(principal);
        return changeRequestService.createRequest(teacherUserId, request.getFieldName(), request.getNewValue());
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
