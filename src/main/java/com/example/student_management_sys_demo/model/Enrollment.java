package com.example.student_management_sys_demo.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "enrollments", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"student_id", "course_id"})
})
public class Enrollment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @Column(name = "enrolled_at", nullable = false)
    private LocalDate enrolledAt = LocalDate.now();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public LocalDate getEnrolledAt() {
        return enrolledAt;
    }

    public void setEnrolledAt(LocalDate enrolledAt) {
        this.enrolledAt = enrolledAt;
    }
}
