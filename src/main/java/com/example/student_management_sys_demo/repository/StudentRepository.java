package com.example.student_management_sys_demo.repository;

import com.example.student_management_sys_demo.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import javax.swing.text.html.Option;

/**
 * Spring Data JPA Repository for Student entity
 *
 * JpaRepository provides built-in CRUD methods:
 * - save(S entity): Create or Update
 * - findById(ID id): Read by ID
 * - findAll(): Read all
 * - deleteById(ID id): Delete by ID
 * - count(): Count records
 * - existsById(ID id): Check existence
 *
 * You can also define custom query methods using:
 * 1. Method naming conventions (like findByEmail)
 * 2. @Query annotation with JPQL
 * 3. @Query annotation with native SQL
 */
@Repository
public interface StudentRepository extends JpaRepository<Student,Long>
//Student is every object and Long is our primary key for identification
{
    //the method is autoimplemented! REMEMBER THE NAMING CONVENTIONS
    Optional<Student> findByEmail(String email);

    List<Student> findByDepartment(String department);

    //finds students by first name but not case sensitive
    List<Student> findByFirstNameContainingIgnoreCase(String firstName);
    //finds students by last name but is case sensitive
    List<Student> findByLastName(String lastName);

    //@Query annotation is used to run custom queries
    //this query finds students by deparment and names sorted
//    @Query("SELECT * FROM students s WHERE s.department= :department ORDER BY s.lastname, s.firstName")
//    List<Student> findByDepartmentOrderByLastNameAsc(@Param("department")String department);
    @Query("SELECT s FROM Student s WHERE s.department = :department ORDER BY s.lastName, s.firstName")
    List<Student> findByDepartmentSorted(@Param("department") String department);
    //@Param is a query parameter used to bind a method parameter to a named parameter in a manually defined query

    // Custom JPQL Query - search students by name or email
//    @Query("SELECT s FROM students s WHERE LOWER(s.firstName) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
//            "OR LOWER(s.lastName) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
//            "OR LOWER(s.email) LIKE LOWER(CONCAT('%', :keyword, '%'))")
//    List<Student> searchStudents(@Param("keyword") String keyword);

    @Query("SELECT s FROM Student s WHERE LOWER(s.firstName) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(s.lastName) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(s.email) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Student> searchStudents(@Param("keyword") String keyword);

    //this is a native sql query
//    @Query(value="SELECT * FROM students WHERE department = ?1 AND phoneno IS NOT NULL", nativeQuery = true)
//    List<Student> searchStudentsByPhoneno(String department);
    @Query(value = "SELECT * FROM students WHERE department = ?1 AND phoneno IS NOT NULL",
            nativeQuery = true)
    List<Student> findByDepartmentWithPhone(String department);

    //check if a email is already existing (can be used for input validation or registration process to avoid duplication)
    boolean existsByEmail(String email);

    //can be used to get count of studentts in a department
    long countByDepartment(String department);

}
