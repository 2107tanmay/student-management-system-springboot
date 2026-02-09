package com.example.student_management_sys_demo.dto;

import java.time.LocalDate;

public record StudentSummary(
        Long id,
        String firstName,
        String lastName,
        String email,
        String department,
        LocalDate enrollmentDate
) {
}