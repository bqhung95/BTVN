package com.example.jb_ioc_pthb260515_buiquanghung.controller;


import com.example.jb_ioc_pthb260515_buiquanghung.dto.EmployeeCreateDTO;
import com.example.jb_ioc_pthb260515_buiquanghung.dto.EmployeeResponseDTO;
import com.example.jb_ioc_pthb260515_buiquanghung.dto.EmployeeUpdateDTO;
import com.example.jb_ioc_pthb260515_buiquanghung.entity.Department;
import com.example.jb_ioc_pthb260515_buiquanghung.entity.Employee;
import com.example.jb_ioc_pthb260515_buiquanghung.service.EmployeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/v1/employees")
@RequiredArgsConstructor
@Slf4j
public class EmployeeController {
    private final EmployeeService employeeService;

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeResponseDTO> getEmployeeById(@PathVariable Long id){
        log.info("REST: GET /api/v1/employees/{}", id);
        return ResponseEntity.ok(employeeService.getEmployeeById(id));
    }

    @PostMapping
    public ResponseEntity<EmployeeResponseDTO> creatEmployee(@Valid @RequestBody EmployeeCreateDTO employeeCreateDTO){
        log.info("REST: POST /api/v1/employees");
        EmployeeResponseDTO createDTO = employeeService.createEmployee(employeeCreateDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmployeeResponseDTO> updateEmployee(
            @PathVariable Long id,
            @Valid @RequestBody EmployeeUpdateDTO dto) {
        log.info("REST: PUT /api/v1/employees/{}", id);
        return ResponseEntity.ok(employeeService.updateEmployee(id, dto));  // HTTP 200
    }

    // DELETE /api/v1/employees/{id} -> Xóa
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        log.info("REST: DELETE /api/v1/employees/{}", id);
        employeeService.deleteEmployee(id);
        return ResponseEntity.noContent().build();               // HTTP 204
    }

    // GET /api/v1/employees?name=...&department=...&minSalary=...&maxSalary=...
    //                       &page=0&size=10&sortBy=fullName&sortDir=asc
    @GetMapping
    public ResponseEntity<Page<EmployeeResponseDTO>> searchEmployees(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Department department,
            @RequestParam(required = false) BigDecimal minSalary,
            @RequestParam(required = false) BigDecimal maxSalary,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        log.info("REST: GET /api/v1/employees - search");
        Page<EmployeeResponseDTO> result = employeeService.searchEmployees(
                name, department, minSalary, maxSalary, page, size, sortBy, sortDir);
        return ResponseEntity.ok(result);                        // HTTP 200
    }
}
