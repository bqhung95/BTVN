package com.example.jb_ioc_pthb260515_buiquanghung.repository.impl;

import com.example.jb_ioc_pthb260515_buiquanghung.dto.EmployeeCreateDTO;
import com.example.jb_ioc_pthb260515_buiquanghung.dto.EmployeeResponseDTO;
import com.example.jb_ioc_pthb260515_buiquanghung.dto.EmployeeUpdateDTO;
import com.example.jb_ioc_pthb260515_buiquanghung.entity.Department;
import com.example.jb_ioc_pthb260515_buiquanghung.entity.Employee;
import com.example.jb_ioc_pthb260515_buiquanghung.exception.DuplicateResourceException;
import com.example.jb_ioc_pthb260515_buiquanghung.exception.ResourceNotFoundException;
import com.example.jb_ioc_pthb260515_buiquanghung.repository.EmployeeRepository;
import com.example.jb_ioc_pthb260515_buiquanghung.service.EmployeeService;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository employeeRepository;


    @Override
    @Transactional
    public EmployeeResponseDTO createEmployee(EmployeeCreateDTO employeeCreateDTO) {
        log.info("Tạo nhân viên mới với mã: {}", employeeCreateDTO.getEmployeeCode());

        if(employeeRepository.existsEmployeeCode(employeeCreateDTO.getEmployeeCode())) {
            log.warn("Trùng mã nhân viên: {}", employeeCreateDTO.getEmployeeCode());
            throw new DuplicateResourceException("Mã nhân viên đã tồn tại: " + employeeCreateDTO.getEmployeeCode());
        }

        if (employeeRepository.existsByEmail(employeeCreateDTO.getEmail())) {
            log.warn("Trùng email: {}", employeeCreateDTO.getEmail());
            throw new DuplicateResourceException(
                    "Email đã tồn tại: " + employeeCreateDTO.getEmail());
        }

        Employee employee = Employee.builder()
                .employeeCode(employeeCreateDTO.getEmployeeCode())
                .fullName(employeeCreateDTO.getFullName())
                .email(employeeCreateDTO.getEmail())
                .phone(employeeCreateDTO.getPhone())
                .department(employeeCreateDTO.getDepartment())
                .position(employeeCreateDTO.getPosition())
                .salary(employeeCreateDTO.getSalary())
                .hireDate(employeeCreateDTO.getHireDate())
                .status(employeeCreateDTO.getStatus())
                .build();

        Employee savedEmployee = employeeRepository.save(employee);
        log.info("Đã tạo nhân viên ID={}", savedEmployee.getEmployeeCode());
        return mapToResponseDTO(savedEmployee);
    }

    @Override
    public EmployeeResponseDTO getEmployeeById(Long id) {
        log.info("Lấy chi tiết nhân viên ID={}", id);
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException
                        ("không tìm thấy nhân viên với ID: " + id));
        return mapToResponseDTO(employee);
    }

    @Override
    @Transactional
    public EmployeeResponseDTO updateEmployee(Long id, EmployeeUpdateDTO employeeUpdateDTO) {
        log.info("Cập nhật nhân viên ID={}", id);
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException(
                        "Không tìm thấy nhân viên với ID: "+ id));

        if(!employee.getEmail().equals(employeeUpdateDTO.getEmail())
        && employeeRepository.existsByEmail(employeeUpdateDTO.getEmail())) {
            throw new DuplicateResourceException(
                    "Email đã tồn tại: " + employeeUpdateDTO.getEmail());
        }

        employee.setFullName(employeeUpdateDTO.getFullName());
        employee.setEmail(employeeUpdateDTO.getEmail());
        employee.setPhone(employeeUpdateDTO.getPhone());
        employee.setDepartment(employeeUpdateDTO.getDepartment());
        employee.setPosition(employeeUpdateDTO.getPosition());
        employee.setSalary(employeeUpdateDTO.getSalary());
        employee.setHireDate(employeeUpdateDTO.getHireDate());
        employee.setStatus(employeeUpdateDTO.getStatus());

        Employee employeeUpdated = employeeRepository.save(employee);
        log.info("Đã cập nhật nhân viên ID={}", id);
        return mapToResponseDTO(employeeUpdated);
    }

    @Override
    @Transactional
    public void deleteEmployee(Long id) {
        log.info("Xóa nhân viên với ID={}", id);
        if(!employeeRepository.existsById(id)) {
            throw new ResourceNotFoundException("Không tìm thấy nhân viên ID: " + id);
        }
        employeeRepository.deleteById(id);
        log.info("Đã xóa nhân viên ID={}", id);
    }

    @Override
    public Page<EmployeeResponseDTO> searchEmployees(String name, Department department, BigDecimal minSalary, BigDecimal maxSalary, int page, int size, String sortBy, String sortDir) {
        log.info("Tìm kiếm nhân viên: name={}, department={}, minSalary={}, maxSalary={}", name, department, minSalary, maxSalary);

        Sort sort = sortDir.equalsIgnoreCase("asc")
                ?Sort.by(sortBy).ascending()
                :Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Specification<Employee> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (name != null && !name.isBlank()) {
                // LIKE %name% và KHÔNG phân biệt hoa thường
                predicates.add(cb.like(cb.lower(root.get("fullName")),
                        "%" + name.toLowerCase() + "%"));
            }
            if (department != null) {
                predicates.add(cb.equal(root.get("department"), department));
            }
            if (minSalary != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("salary"), minSalary));
            }
            if (maxSalary != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("salary"), maxSalary));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };

        Page<Employee> pageResult = employeeRepository.findAll(spec, pageable);
        return pageResult.map(this::mapToResponseDTO);   // Chuyển Entity -> DTO

}


    private EmployeeResponseDTO mapToResponseDTO(Employee e) {
        return EmployeeResponseDTO.builder()
                .id(e.getId())
                .employeeCode(e.getEmployeeCode())
                .fullName(e.getFullName())
                .email(e.getEmail())
                .phone(e.getPhone())
                .department(e.getDepartment())
                .position(e.getPosition())
                .salary(e.getSalary())
                .hireDate(e.getHireDate())
                .status(e.getStatus())
                .createdAt(e.getCreatedAt())
                .updatedAt(e.getUpdatedAt())
                .build();
    }
}
