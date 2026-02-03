package com.example.student_management_sys_demo.services;

import com.example.student_management_sys_demo.model.Student;
import com.example.student_management_sys_demo.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
//@Service is used to signify this is a service class to spring bean factory
@Transactional
//this annotation in used to specify that an interface/class/method must be transactional semantics
//eg: start a brand new transaction everytime
public class StudentServiceImplementer implements StudentService {

    private final StudentRepository studentRepository;

    //constructor based injection XD mentos zindagis
    @Autowired
    public StudentServiceImplementer(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public Student createStudent(Student student) {
        if (student.getEnrollmentDate() == null) student.setEnrollmentDate(LocalDate.now());

        if (studentRepository.existsByEmail(student.getEmail())) {
            throw new RuntimeException("Email already exists!!!: " + student.getEmail());
        }

        return studentRepository.save(student);
        //save method is inbuilt in jpa and used to save the progress made to student object yet
    }

    @Override
    @Transactional(readOnly = true)
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Student> getStudentById(String id) {
        return studentRepository.findById(Long.parseLong(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Student> searchStudents(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return studentRepository.findAll();
        }
        return studentRepository.searchStudents(keyword);
    }

    @Override
    public Student updateStudent(long studentId, Student studentDetails) {
        Student student = studentRepository.findById(studentId).orElseThrow(() -> new RuntimeException("Student not found"));
        //basically i check if there is a student as this method returns optional i can throw inplace a exception if nothing is returned
        //i did this using orElseThrow method and lambda in place

        //below: we check if a mail is being changed to new mail and that new mail already exists in the system
        if (!student.getEmail().equals(studentDetails.getEmail()) && studentRepository.existsByEmail(studentDetails.getEmail())) {
            throw new RuntimeException("Email already exists!!!: " + studentDetails.getEmail() + "\ntry new mail");

        }
        //update actual values after checking the breakpoints
        student.setFirstName(studentDetails.getFirstName());
        student.setLastName(studentDetails.getLastName());
        student.setEmail(studentDetails.getEmail());
        student.setDateOfBirth(studentDetails.getDateOfBirth());
        student.setPhoneNumber(studentDetails.getPhoneNumber());
        student.setDepartment(studentDetails.getDepartment());
        return studentRepository.save(student);
    }

    @Override
    public void deleteStudent(long studentId) {
        if(!studentRepository.existsById(studentId)) throw new RuntimeException("Student not found");
        studentRepository.deleteById(studentId);
    }

    @Override
    @Transactional(readOnly = true)
    public long getStudentCountByDepartment(String department) {
        return studentRepository.countByDepartment(department);
    }

    @Transactional(readOnly = true)
    public List<Student> getStudentsByDepartment(String department) {
        return studentRepository.findByDepartment(department);
    }

}
