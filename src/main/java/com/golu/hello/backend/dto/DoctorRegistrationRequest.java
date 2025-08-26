package com.golu.hello.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DoctorRegistrationRequest {
    private String username;
    private String password;
    private String name;
    private String specialization;
    private String phone;
    private String email;
    private String qualification;
    private Integer experience;
    private String address;
    private BigDecimal consultationFee;
}
