package com.example.student_management_sys_demo.services;

import com.example.student_management_sys_demo.model.Attendance;
import com.example.student_management_sys_demo.model.Course;
import com.example.student_management_sys_demo.model.Student;
import com.example.student_management_sys_demo.model.Teacher;
import com.example.student_management_sys_demo.repository.AttendanceRepository;
import com.example.student_management_sys_demo.repository.CourseRepository;
import com.example.student_management_sys_demo.repository.StudentRepository;
import com.example.student_management_sys_demo.repository.TeacherRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;

@Service
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;

    public AttendanceService(AttendanceRepository attendanceRepository,
                             TeacherRepository teacherRepository,
                             StudentRepository studentRepository,
                             CourseRepository courseRepository) {
        this.attendanceRepository = attendanceRepository;
        this.teacherRepository = teacherRepository;
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
    }

    public Attendance markAttendance(Long teacherUserId, Long studentId, Long courseId, LocalDate date, String status) {
        Teacher teacher = teacherRepository.findByUserId(teacherUserId)
                .orElseThrow(() -> new NoSuchElementException("Teacher not found"));
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new NoSuchElementException("Student not found"));
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new NoSuchElementException("Course not found"));

        Attendance attendance = new Attendance();
        attendance.setTeacher(teacher);
        attendance.setStudent(student);
        attendance.setCourse(course);
        attendance.setAttendanceDate(date);
        attendance.setStatus(status);
        return attendanceRepository.save(attendance);
    }

    public List<Attendance> getStudentAttendance(Long studentUserId) {
        Student student = studentRepository.findByUserId(studentUserId)
                .orElseThrow(() -> new NoSuchElementException("Student not found"));
        return attendanceRepository.findByStudentId(student.getId());
    }
}
