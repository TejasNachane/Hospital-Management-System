package com.golu.hello.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentStatusUpdate {
    private Long appointmentId;
    private String status; // APPROVED, REJECTED
    private String notes;
}
