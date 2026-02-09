package com.example.student_management_sys_demo.repository;

import com.example.student_management_sys_demo.model.Attendance;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    List<Attendance> findByStudentId(Long studentId);
    List<Attendance> findByTeacherIdAndAttendanceDate(Long teacherId, LocalDate attendanceDate);
}
