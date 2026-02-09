package com.example.student_management_sys_demo.services;

import com.example.student_management_sys_demo.model.Course;
import com.example.student_management_sys_demo.model.Grade;
import com.example.student_management_sys_demo.model.Student;
import com.example.student_management_sys_demo.model.Teacher;
import com.example.student_management_sys_demo.repository.CourseRepository;
import com.example.student_management_sys_demo.repository.GradeRepository;
import com.example.student_management_sys_demo.repository.StudentRepository;
import com.example.student_management_sys_demo.repository.TeacherRepository;
import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;

@Service
public class GradeService {

    private final GradeRepository gradeRepository;
    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;

    public GradeService(GradeRepository gradeRepository,
                        TeacherRepository teacherRepository,
                        StudentRepository studentRepository,
                        CourseRepository courseRepository) {
        this.gradeRepository = gradeRepository;
        this.teacherRepository = teacherRepository;
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
    }

    public Grade gradeStudent(Long teacherUserId, Long studentId, Long courseId, Double score, String remarks) {
        Teacher teacher = teacherRepository.findByUserId(teacherUserId)
                .orElseThrow(() -> new NoSuchElementException("Teacher not found"));
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new NoSuchElementException("Student not found"));
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new NoSuchElementException("Course not found"));

        Grade grade = new Grade();
        grade.setTeacher(teacher);
        grade.setStudent(student);
        grade.setCourse(course);
        grade.setScore(score);
        grade.setRemarks(remarks);
        return gradeRepository.save(grade);
    }

    public List<Grade> getStudentGrades(Long studentUserId) {
        Student student = studentRepository.findByUserId(studentUserId)
                .orElseThrow(() -> new NoSuchElementException("Student not found"));
        return gradeRepository.findByStudentId(student.getId());
    }
}
