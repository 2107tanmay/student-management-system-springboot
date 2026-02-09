package com.example.student_management_sys_demo.repository;

import com.example.student_management_sys_demo.model.ChangeRequest;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChangeRequestRepository extends JpaRepository<ChangeRequest, Long> {
    List<ChangeRequest> findByRequesterId(Long requesterId);
}