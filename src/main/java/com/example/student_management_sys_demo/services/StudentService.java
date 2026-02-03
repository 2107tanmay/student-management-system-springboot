package com.example.student_management_sys_demo.services;


import com.example.student_management_sys_demo.model.Student;

import java.util.List;
import java.util.Optional;

public interface StudentService {
    //create
    Student createStudent(Student student);

    //read
    List<Student> getAllStudents();
    Optional<Student> getStudentById(long id);
    List<Student> searchStudents(String keyword);
    List<Student> getStudentsByDeparment(String department);

    //update
    Student updateStudent(long id, Student student);

    //delete
    void deleteStudent(long id);

    //additional functionalities
    boolean emailExists(String email);
    long getStudentCountByDepartment(String department);


    Optional<Student> getStudentById(String id);
}
