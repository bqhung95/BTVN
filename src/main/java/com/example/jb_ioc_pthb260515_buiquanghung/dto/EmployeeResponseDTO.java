package com.example.jb_ioc_pthb260515_buiquanghung.dto;


import com.example.jb_ioc_pthb260515_buiquanghung.entity.Department;
import com.example.jb_ioc_pthb260515_buiquanghung.entity.EmployeeStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeResponseDTO {
    private Long id;
    private String employeeCode;
    private String fullName;
    private String email;
    private String phone;
    private Department department;
    private String position;
    private BigDecimal salary;
    private LocalDate hireDate;
    private EmployeeStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
