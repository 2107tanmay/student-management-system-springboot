package com.example.student_management_sys_demo.services;

import com.example.student_management_sys_demo.dto.CreateStudentRequest;
import com.example.student_management_sys_demo.dto.CreateTeacherRequest;
import com.example.student_management_sys_demo.model.ChangeRequest;
import com.example.student_management_sys_demo.model.RequestStatus;
import com.example.student_management_sys_demo.model.Role;
import com.example.student_management_sys_demo.model.Student;
import com.example.student_management_sys_demo.model.Teacher;
import com.example.student_management_sys_demo.model.User;
import com.example.student_management_sys_demo.repository.ChangeRequestRepository;
import com.example.student_management_sys_demo.repository.StudentRepository;
import com.example.student_management_sys_demo.repository.TeacherRepository;
import com.example.student_management_sys_demo.repository.UserRepository;
import java.util.NoSuchElementException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AdminService {

    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final ChangeRequestRepository changeRequestRepository;
    private final PasswordEncoder passwordEncoder;
    private final String defaultProfileImage;

    public AdminService(UserRepository userRepository,
                        StudentRepository studentRepository,
                        TeacherRepository teacherRepository,
                        ChangeRequestRepository changeRequestRepository,
                        PasswordEncoder passwordEncoder,
                        @Value("${app.profile.default-image}") String defaultProfileImage) {
        this.userRepository = userRepository;
        this.studentRepository = studentRepository;
        this.teacherRepository = teacherRepository;
        this.changeRequestRepository = changeRequestRepository;
        this.passwordEncoder = passwordEncoder;
        this.defaultProfileImage = defaultProfileImage;
    }

    @Transactional
    public Student createStudent(CreateStudentRequest request) {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.STUDENT);
        user.setProfileImageUrl(defaultProfileImage);
        userRepository.save(user);

        Student student = new Student();
        student.setFirstName(request.getFirstName());
        student.setLastName(request.getLastName());
        student.setEmail(request.getEmail());
        student.setDepartment(request.getDepartment());
        student.setUser(user);
        return studentRepository.save(student);
    }

    @Transactional
    public Teacher createTeacher(CreateTeacherRequest request) {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.TEACHER);
        user.setProfileImageUrl(defaultProfileImage);
        userRepository.save(user);

        Teacher teacher = new Teacher();
        teacher.setFirstName(request.getFirstName());
        teacher.setLastName(request.getLastName());
        teacher.setEmail(request.getEmail());
        teacher.setDepartment(request.getDepartment());
        teacher.setUser(user);
        return teacherRepository.save(teacher);
    }

    @Transactional
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }

    @Transactional
    public ChangeRequest approveChangeRequest(Long requestId) {
        ChangeRequest changeRequest = changeRequestRepository.findById(requestId)
                .orElseThrow(() -> new NoSuchElementException("Change request not found"));
        changeRequest.setStatus(RequestStatus.APPROVED);
        applyChange(changeRequest);
        return changeRequestRepository.save(changeRequest);
    }

    @Transactional
    public ChangeRequest rejectChangeRequest(Long requestId) {
        ChangeRequest changeRequest = changeRequestRepository.findById(requestId)
                .orElseThrow(() -> new NoSuchElementException("Change request not found"));
        changeRequest.setStatus(RequestStatus.REJECTED);
        return changeRequestRepository.save(changeRequest);
    }

    public Iterable<ChangeRequest> listChangeRequests() {
        return changeRequestRepository.findAll();
    }

    private void applyChange(ChangeRequest changeRequest) {
        User requester = changeRequest.getRequester();
        if (requester.getRole() == Role.STUDENT) {
            Student student = studentRepository.findByUserId(requester.getId())
                    .orElseThrow(() -> new NoSuchElementException("Student not found"));
            applyStudentChange(student, changeRequest);
            studentRepository.save(student);
        } else if (requester.getRole() == Role.TEACHER) {
            Teacher teacher = teacherRepository.findByUserId(requester.getId())
                    .orElseThrow(() -> new NoSuchElementException("Teacher not found"));
            applyTeacherChange(teacher, changeRequest);
            teacherRepository.save(teacher);
        }
    }

    private void applyStudentChange(Student student, ChangeRequest changeRequest) {
        switch (changeRequest.getFieldName()) {
            case "firstName" -> student.setFirstName(changeRequest.getNewValue());
            case "lastName" -> student.setLastName(changeRequest.getNewValue());
            case "email" -> student.setEmail(changeRequest.getNewValue());
            case "department" -> student.setDepartment(changeRequest.getNewValue());
            default -> throw new IllegalArgumentException("Unsupported field for student change");
        }
    }

    private void applyTeacherChange(Teacher teacher, ChangeRequest changeRequest) {
        switch (changeRequest.getFieldName()) {
            case "firstName" -> teacher.setFirstName(changeRequest.getNewValue());
            case "lastName" -> teacher.setLastName(changeRequest.getNewValue());
            case "email" -> teacher.setEmail(changeRequest.getNewValue());
            case "department" -> teacher.setDepartment(changeRequest.getNewValue());
            default -> throw new IllegalArgumentException("Unsupported field for teacher change");
        }
    }
}
