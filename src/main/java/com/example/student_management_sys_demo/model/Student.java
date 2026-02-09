package com.example.student_management_sys_demo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

/**
 * JPA Entity representing a Student
 * This class demonstrates key JPA concepts:
 * - @Entity: Marks this class as a JPA entity
 * - @Table: Specifies the table name in the database
 * - @Id: Marks the primary key
 * - @GeneratedValue: Auto-generates primary key values
 * - @Column: Customizes column mapping
 */
@Entity
@Table(name="students")
public class Student {

    @OneToOne(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private StudentProfile profile;

    public StudentProfile getProfile() {
        return profile;
    }

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", unique = true)
    private User user;

    public void setProfile(StudentProfile profile) {
        if (profile == null) {
            if (this.profile != null) {
                this.profile.setStudent(null);
            }
        } else {
            profile.setStudent(this);
        }
        this.profile = profile;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


    // Primary Key with Auto-increment
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "First name is required.")
    @Size(max = 50, message = "First name cannot exceed 50 characters.")
    @Column(name="first_name", nullable=false, length = 50)
    private String firstName;

    @NotBlank(message = "Last name is required.")
    @Size(max = 50, message = "Last name cannot exceed 50 characters.")
    @Column(name="last_name", nullable = false, length = 50)
    private String lastName;

    @NotBlank(message = "Email is required.")
    @Email(message = "Enter a valid email address.")
    @Size(max = 100, message = "Email cannot exceed 100 characters.")
    @Column(name ="email", unique = true, length = 100)
    private String email;

    @Past(message = "Date of birth must be in the past.")
    @Column(name = "dob")
    private LocalDate dob;

    @Pattern(regexp = "^$|^[0-9+()\\-\\s]{7,20}$", message = "Phone number must be 7-20 digits and may include +, -, or spaces.")
    @Column(name="phoneno", length = 20)
    private String phoneno;

    @Size(max = 50, message = "Department cannot exceed 50 characters.")
    @Column(name="department", length =50)
    private String department;

    @Column(name="add_date")
    private LocalDate addDate; //addmission date

    public Student(String firstName, String lastName, String email) {
        this.addDate = LocalDate.now(); //add student on today's date
        this.firstName = firstName;
        this.lastName = lastName;
        this.email=email;
    }

    public Student() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getDateOfBirth() {
        return dob;
    }

    public void setDateOfBirth(LocalDate dob) {
        this.dob = dob;
    }

    public String getPhoneNumber() {
        return phoneno;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneno = phoneNumber;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public LocalDate getEnrollmentDate() {
        return addDate;
    }

    public void setEnrollmentDate(LocalDate addDate) {
        this.addDate = addDate;
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", department='" + department + '\'' +
                '}';
    }
}
