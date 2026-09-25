package com.example.jb_ioc_pthb260515_buiquanghung.service;

import com.example.jb_ioc_pthb260515_buiquanghung.dto.EmployeeCreateDTO;
import com.example.jb_ioc_pthb260515_buiquanghung.dto.EmployeeResponseDTO;
import com.example.jb_ioc_pthb260515_buiquanghung.dto.EmployeeUpdateDTO;
import com.example.jb_ioc_pthb260515_buiquanghung.entity.Department;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;

public interface EmployeeService {
    EmployeeResponseDTO createEmployee(EmployeeCreateDTO employeeCreateDTO);

    EmployeeResponseDTO getEmployeeById(Long id);

    EmployeeResponseDTO updateEmployee(Long id, EmployeeUpdateDTO employeeUpdateDTO);

    void deleteEmployee(Long id);

    Page<EmployeeResponseDTO> searchEmployees(String name, Department department,
                                              BigDecimal minSalary, BigDecimal maxSalary,
                                              int page, int size,
                                              String sortBy, String sortDir);
}
