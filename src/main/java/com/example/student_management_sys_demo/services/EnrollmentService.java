package com.example.student_management_sys_demo.services;

import com.example.student_management_sys_demo.model.Course;
import com.example.student_management_sys_demo.model.Enrollment;
import com.example.student_management_sys_demo.model.Student;
import com.example.student_management_sys_demo.repository.CourseRepository;
import com.example.student_management_sys_demo.repository.EnrollmentRepository;
import com.example.student_management_sys_demo.repository.StudentRepository;
import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;

    public EnrollmentService(EnrollmentRepository enrollmentRepository,
                             StudentRepository studentRepository,
                             CourseRepository courseRepository) {
        this.enrollmentRepository = enrollmentRepository;
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
    }

    @Transactional
    public Enrollment enrollStudent(Long studentUserId, Long courseId) {
        Student student = studentRepository.findByUserId(studentUserId)
                .orElseThrow(() -> new NoSuchElementException("Student not found"));
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new NoSuchElementException("Course not found"));

        if (enrollmentRepository.existsByStudentIdAndCourseId(student.getId(), courseId)) {
            throw new IllegalStateException("Student already enrolled");
        }

        long enrolledCount = enrollmentRepository.countByCourseId(courseId);
        if (enrolledCount >= Course.MAX_STUDENTS) {
            throw new IllegalStateException("Course is full");
        }

        Enrollment enrollment = new Enrollment();
        enrollment.setStudent(student);
        enrollment.setCourse(course);
        return enrollmentRepository.save(enrollment);
    }

    public List<Enrollment> getStudentEnrollments(Long studentUserId) {
        Student student = studentRepository.findByUserId(studentUserId)
                .orElseThrow(() -> new NoSuchElementException("Student not found"));
        return enrollmentRepository.findByStudentId(student.getId());
    }
}
