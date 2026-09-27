package com.example.jb_ioc_pthb260515_buiquanghung.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErrorResponse {
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;      // Thời điểm lỗi
    private int status;                   // HTTP status code
    private String error;                 // Tên loại lỗi (Not Found, Bad Request...)
    private String message;               // Thông báo chính
    private String path;                  // Endpoint gây lỗi
    private Map<String, String> details;  // Chi tiết lỗi từng field (validation)
}
