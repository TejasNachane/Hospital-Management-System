package com.golu.hello.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminRegistrationRequest {
    private String username;
    private String password;
    private String email;
    private String firstName;
    private String lastName;
}
