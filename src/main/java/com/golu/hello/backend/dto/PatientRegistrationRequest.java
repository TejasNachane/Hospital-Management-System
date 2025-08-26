package com.golu.hello.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PatientRegistrationRequest {
    private String username;
    private String password;
    private String name;
    private Integer age;
    private String gender;
    private String phone;
    private String email;
    private String address;
    private LocalDate dateOfBirth;
    private String bloodGroup;
    private String emergencyContact;
    private String medicalHistory;
}
