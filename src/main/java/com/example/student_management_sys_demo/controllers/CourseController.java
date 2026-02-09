package com.example.student_management_sys_demo.controllers;

import com.example.student_management_sys_demo.model.Course;
import com.example.student_management_sys_demo.services.CourseService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/courses")
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping
    public List<Course> listAvailableCourses() {
        return courseService.listAvailableCourses();
    }
}