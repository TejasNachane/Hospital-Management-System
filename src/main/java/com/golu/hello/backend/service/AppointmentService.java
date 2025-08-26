package com.golu.hello.backend.service;

import com.golu.hello.backend.dto.AppointmentRequest;
import com.golu.hello.backend.dto.AppointmentResponse;
import com.golu.hello.backend.dto.AppointmentStatusUpdate;
import com.golu.hello.backend.entity.Appointment;
import com.golu.hello.backend.entity.Doctor;
import com.golu.hello.backend.entity.Patient;
import com.golu.hello.backend.repository.AppointmentRepository;
import com.golu.hello.backend.repository.DoctorRepository;
import com.golu.hello.backend.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AppointmentService {
    
    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    
    public List<AppointmentResponse> getAllAppointments() {
        return appointmentRepository.findAllWithDetails()
                .stream()
                .map(AppointmentResponse::fromEntity)
                .collect(Collectors.toList());
    }
    
    public Optional<AppointmentResponse> getAppointmentById(Long id) {
        return appointmentRepository.findByIdWithDetails(id)
                .map(AppointmentResponse::fromEntity);
    }
    
    public List<AppointmentResponse> getAppointmentsByPatientId(Long patientId) {
        return appointmentRepository.findByPatientIdWithDetails(patientId)
                .stream()
                .map(AppointmentResponse::fromEntity)
                .collect(Collectors.toList());
    }
    
    public List<AppointmentResponse> getAppointmentsByDoctorId(Long doctorId) {
        return appointmentRepository.findByDoctorIdWithDetails(doctorId)
                .stream()
                .map(AppointmentResponse::fromEntity)
                .collect(Collectors.toList());
    }
    
    public List<AppointmentResponse> getAppointmentsByStatus(String status) {
        return appointmentRepository.findByStatusWithDetails(status)
                .stream()
                .map(AppointmentResponse::fromEntity)
                .collect(Collectors.toList());
    }
    
    public List<AppointmentResponse> getAppointmentsByPatientIdAndStatus(Long patientId, String status) {
        return appointmentRepository.findByPatientIdAndStatus(patientId, status)
                .stream()
                .map(AppointmentResponse::fromEntity)
                .collect(Collectors.toList());
    }
    
    public List<AppointmentResponse> getAppointmentsByDoctorIdAndStatus(Long doctorId, String status) {
        return appointmentRepository.findByDoctorIdAndStatus(doctorId, status)
                .stream()
                .map(AppointmentResponse::fromEntity)
                .collect(Collectors.toList());
    }
    
    public List<AppointmentResponse> getAppointmentsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return appointmentRepository.findByAppointmentTimeBetween(startDate, endDate)
                .stream()
                .map(AppointmentResponse::fromEntity)
                .collect(Collectors.toList());
    }
    
    public List<AppointmentResponse> getAppointmentsByDoctorAndDateRange(Long doctorId, LocalDateTime startDate, LocalDateTime endDate) {
        return appointmentRepository.findByDoctorIdAndAppointmentTimeBetween(doctorId, startDate, endDate)
                .stream()
                .map(AppointmentResponse::fromEntity)
                .collect(Collectors.toList());
    }
    
    public String createAppointment(AppointmentRequest request) {
        Optional<Patient> patientOpt = patientRepository.findById(request.getPatientId());
        if (patientOpt.isEmpty()) {
            return "Patient not found";
        }
        
        Optional<Doctor> doctorOpt = doctorRepository.findById(request.getDoctorId());
        if (doctorOpt.isEmpty()) {
            return "Doctor not found";
        }
        
        // Check if the appointment time is in the future
        if (request.getAppointmentTime().isBefore(LocalDateTime.now())) {
            return "Appointment time must be in the future";
        }
        
        // Check for conflicting appointments for the doctor
        List<Appointment> conflictingAppointments = appointmentRepository.findByDoctorIdAndAppointmentTimeBetween(
            request.getDoctorId(),
            request.getAppointmentTime().minusMinutes(30),
            request.getAppointmentTime().plusMinutes(30)
        );
        
        boolean hasConflict = conflictingAppointments.stream()
            .anyMatch(appointment -> "APPROVED".equals(appointment.getStatus()));
        
        if (hasConflict) {
            return "Doctor is not available at the requested time";
        }
        
        Appointment appointment = new Appointment();
        appointment.setPatient(patientOpt.get());
        appointment.setDoctor(doctorOpt.get());
        appointment.setAppointmentTime(request.getAppointmentTime());
        appointment.setReason(request.getReason());
        appointment.setNotes(request.getNotes());
        appointment.setStatus(request.getStatus() != null ? request.getStatus() : "PENDING");
        
        appointmentRepository.save(appointment);
        return "Appointment created successfully";
    }
    
    public String updateAppointment(Long id, AppointmentRequest request) {
        Optional<Appointment> appointmentOpt = appointmentRepository.findById(id);
        if (appointmentOpt.isEmpty()) {
            return "Appointment not found";
        }
        
        Optional<Patient> patientOpt = patientRepository.findById(request.getPatientId());
        if (patientOpt.isEmpty()) {
            return "Patient not found";
        }
        
        Optional<Doctor> doctorOpt = doctorRepository.findById(request.getDoctorId());
        if (doctorOpt.isEmpty()) {
            return "Doctor not found";
        }
        
        // Check if the appointment time is in the future (only for scheduled appointments)
        if ("SCHEDULED".equals(request.getStatus()) && request.getAppointmentTime().isBefore(LocalDateTime.now())) {
            return "Scheduled appointment time must be in the future";
        }
        
        Appointment appointment = appointmentOpt.get();
        appointment.setPatient(patientOpt.get());
        appointment.setDoctor(doctorOpt.get());
        appointment.setAppointmentTime(request.getAppointmentTime());
        appointment.setReason(request.getReason());
        appointment.setNotes(request.getNotes());
        appointment.setStatus(request.getStatus() != null ? request.getStatus() : appointment.getStatus());
        
        appointmentRepository.save(appointment);
        return "Appointment updated successfully";
    }
    
    public String updateAppointmentStatus(AppointmentStatusUpdate update) {
        Optional<Appointment> appointmentOpt = appointmentRepository.findById(update.getAppointmentId());
        if (appointmentOpt.isEmpty()) {
            return "Appointment not found";
        }
        
        Appointment appointment = appointmentOpt.get();
        appointment.setStatus(update.getStatus());
        appointment.setNotes(update.getNotes());
        
        appointmentRepository.save(appointment);
        return "Appointment status updated successfully";
    }
    
    public void deleteAppointment(Long id) {
        appointmentRepository.deleteById(id);
    }
}
