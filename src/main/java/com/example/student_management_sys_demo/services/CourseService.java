package com.example.student_management_sys_demo.services;

import com.example.student_management_sys_demo.model.Course;
import com.example.student_management_sys_demo.model.Teacher;
import com.example.student_management_sys_demo.repository.CourseRepository;
import com.example.student_management_sys_demo.repository.EnrollmentRepository;
import com.example.student_management_sys_demo.repository.TeacherRepository;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class CourseService {

    private final CourseRepository courseRepository;
    private final TeacherRepository teacherRepository;
    private final EnrollmentRepository enrollmentRepository;

    public CourseService(CourseRepository courseRepository,
                         TeacherRepository teacherRepository,
                         EnrollmentRepository enrollmentRepository) {
        this.courseRepository = courseRepository;
        this.teacherRepository = teacherRepository;
        this.enrollmentRepository = enrollmentRepository;
    }

    public Course createCourse(Long teacherUserId, String name, String subject) {
        Teacher teacher = teacherRepository.findByUserId(teacherUserId)
                .orElseThrow(() -> new NoSuchElementException("Teacher not found"));
        Course course = new Course();
        course.setName(name);
        course.setSubject(subject);
        course.setTeacher(teacher);
        return courseRepository.save(course);
    }

    public List<Course> listAvailableCourses() {
        return courseRepository.findAll().stream()
                .filter(course -> enrollmentRepository.countByCourseId(course.getId()) < Course.MAX_STUDENTS)
                .collect(Collectors.toList());
    }
}
