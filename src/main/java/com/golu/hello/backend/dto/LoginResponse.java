package com.golu.hello.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
    private String token;
    private String username;
    private String role;
    private Long userId;
    private Long doctorId;    // Only populated for doctor role
    private Long patientId;   // Only populated for patient role
    private String message;
}
