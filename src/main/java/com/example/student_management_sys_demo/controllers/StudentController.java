package com.example.student_management_sys_demo.controllers;

import com.example.student_management_sys_demo.exceptions.ResourceNotFoundException;
import com.example.student_management_sys_demo.model.Student;
import com.example.student_management_sys_demo.services.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

//@Controller: marks this as a controller
//@RequestMapping() will map http requests to hndler methods
//model: used to pass our data to views
@Controller
@RequestMapping("/students")
public class StudentController {
    private StudentService studentService;

    @Autowired
    public void setStudentService(StudentService studentService) {
        this.studentService = studentService;
    }

    //it will list all students at -- get /students
    @GetMapping
    public String listStudents(Model model, @RequestParam(required = false) String keyword, @RequestParam(required = false) String department ) {
        List<Student> students;

        if(keyword != null && !keyword.trim().isEmpty()){
            students = studentService.searchStudents(keyword);
            model.addAttribute("keyword", keyword);
        }
        else if(department != null && !department.trim().isEmpty()){
            students = studentService.getStudentsByDepartment(department);
            model.addAttribute("department", department);
        }
        else{
            students = studentService.getAllStudents();
        }

        model.addAttribute("students", students);
        return "student-list";
    }

    //show form to create new student - get /students/new
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("student", new Student());
        model.addAttribute("pageTitle", "Add New Student");
        return "student-form";
    }

    //create new student /students
    @PostMapping
    public String createStudent( @ModelAttribute("student") Student student,
                                BindingResult bindingResult,
                                RedirectAttributes redirectAttributes,
                                Model model){

        if(bindingResult.hasErrors()){
            model.addAttribute("pageTitle", "Add New Student");
            return "student-form";
        }

        studentService.createStudent(student);
        redirectAttributes.addFlashAttribute("successMessage", "Student Created Successfully");
        return "redirect:/students";
    }

    //show form to edit student /students/edit/{id}
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Student student = studentService.getStudentById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student with id " + id + " not found"));
        model.addAttribute("student", student);
        model.addAttribute("pageTitle", "Edit Student");
        return "student-form";
    }

    //Update Student post /students/{id}
    @PostMapping("/{id}")
    public String updateStudent(@PathVariable Long id,
                                @ModelAttribute("student") Student student,
                                BindingResult bindingResult,
                                RedirectAttributes redirectAttributes,
                                Model model){
        if(bindingResult.hasErrors()){
            model.addAttribute("pageTitle", "Edit Student");
            return "student-form";
        }

        studentService.updateStudent(id, student);
        redirectAttributes.addFlashAttribute("successMessage", "Student Updated Successfully");
        return "redirect:/students";
    }

    //view student details /students/{id}
    @GetMapping("/{id}")
    public String viewStudent(@PathVariable Long id, Model model) {
        Student student = studentService.getStudentById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student with id " + id + " not found"));
        model.addAttribute("student", student);
        return "student-details";
    }

    //delete student - get /students/delete/{id}
    @GetMapping("/delete/{id}")
    public String deleteStudent(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        studentService.deleteStudent(id);
        redirectAttributes.addFlashAttribute("successMessage", "Student Deleted Successfully");
        return "redirect:/students";
    }

}
