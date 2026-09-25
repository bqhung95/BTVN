package com.example.jb_ioc_pthb260515_buiquanghung.dto;


import com.example.jb_ioc_pthb260515_buiquanghung.entity.Department;
import com.example.jb_ioc_pthb260515_buiquanghung.entity.EmployeeStatus;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class EmployeeCreateDTO {
    @NotBlank(message = "Mã nhân viên không để trống")
    @Pattern(regexp = "EMP-\\d{4}", message = "Mã nhân viên phải có định danh EMP-XXXX")
    private String employeeCode;

    @NotBlank(message = "Họ tên không để trống")
    @Size(min=3, max = 50, message = "Họ tên từ 3-50 kí tự")
    private String fullName;

    @NotBlank(message = "Email không để trống")
    @Email(message = "Email không đúng định dạng")
    private String email;

    @NotBlank(message = "Số điện thoại không để trống")
    @Pattern(regexp = "0\\d{9}", message = "SĐT bắt đầu bằng 0 và có 10 chữ số")
    private String phone;

    @NotBlank(message = "Phòng ban không để trống")
    private Department department;

    private String position;

    @NotBlank(message = "Lương không để trống")
    @DecimalMin(value = "0.0", inclusive = false, message = "Lương phải lớn hơn 0")
    private BigDecimal salary;

    @NotBlank(message = "Ngày vào làm không để trống")
    @PastOrPresent(message = "Ngày vào làm bằng ngày hiện tại")
    private LocalDate hireDate;

    @NotBlank(message = "Trạng thái không để trống")
    private EmployeeStatus status;
}
