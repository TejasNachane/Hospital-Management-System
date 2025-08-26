package com.golu.hello.backend.service;

import com.golu.hello.backend.config.PasswordConfig;
import com.golu.hello.backend.dto.*;
import com.golu.hello.backend.entity.Doctor;
import com.golu.hello.backend.entity.Patient;
import com.golu.hello.backend.entity.Role;
import com.golu.hello.backend.entity.User;
import com.golu.hello.backend.repository.DoctorRepository;
import com.golu.hello.backend.repository.PatientRepository;
import com.golu.hello.backend.repository.RoleRepository;
import com.golu.hello.backend.repository.UserRepository;
import com.golu.hello.backend.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    
    public LoginResponse login(LoginRequest loginRequest) {
        log.info("Login attempt for user: {}", loginRequest.getUsername());
        
        Optional<User> userOpt = userRepository.findByUsername(loginRequest.getUsername());
        
        if (userOpt.isEmpty()) {
            log.warn("Login failed - User not found: {}", loginRequest.getUsername());
            return new LoginResponse(null, null, null, null, null, null, "User not found");
        }
        
        User user = userOpt.get();
        
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            log.warn("Login failed - Invalid credentials for user: {}", loginRequest.getUsername());
            return new LoginResponse(null, null, null, null, null, null, "Invalid credentials");
        }
        
        String token = jwtUtil.generateToken(user.getUsername(), user.getRole().getName(), user.getId());
        
        // Get role-specific IDs
        Long doctorId = null;
        Long patientId = null;
        
        if ("DOCTOR".equals(user.getRole().getName())) {
            Optional<Doctor> doctorOpt = doctorRepository.findByUserId(user.getId());
            if (doctorOpt.isPresent()) {
                doctorId = doctorOpt.get().getId();
            }
        } else if ("PATIENT".equals(user.getRole().getName())) {
            Optional<Patient> patientOpt = patientRepository.findByUserId(user.getId());
            if (patientOpt.isPresent()) {
                patientId = patientOpt.get().getId();
            }
        }
        
        log.info("Login successful for user: {} with role: {}", user.getUsername(), user.getRole().getName());
        return new LoginResponse(token, user.getUsername(), user.getRole().getName(), user.getId(), doctorId, patientId, "Login successful");
    }
    
    @Transactional
    public String registerAdmin(AdminRegistrationRequest request) {
        log.info("Admin registration attempt for username: {}", request.getUsername());
        
        if (userRepository.existsByUsername(request.getUsername())) {
            log.warn("Admin registration failed - Username already exists: {}", request.getUsername());
            return "Username already exists";
        }
        
        if (userRepository.existsByEmail(request.getEmail())) {
            log.warn("Admin registration failed - Email already exists: {}", request.getEmail());
            return "Email already exists";
        }
        
        // Validate password strength
        if (!PasswordConfig.isValidPassword(request.getPassword())) {
            log.warn("Admin registration failed - Weak password for username: {}", request.getUsername());
            return PasswordConfig.getPasswordRequirements();
        }
        
        Optional<Role> adminRoleOpt = roleRepository.findByName("ADMIN");
        if (adminRoleOpt.isEmpty()) {
            log.error("Admin registration failed - Admin role not found");
            return "Admin role not found";
        }
        
        try {
            // Create user
            User user = new User();
            user.setUsername(request.getUsername());
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            user.setEmail(request.getEmail());
            user.setFirstName(request.getFirstName());
            user.setLastName(request.getLastName());
            user.setRole(adminRoleOpt.get());
            
            userRepository.save(user);
            
            log.info("Admin registered successfully: {}", request.getUsername());
            return "Admin registered successfully";
            
        } catch (Exception e) {
            log.error("Admin registration failed for username: {} - Error: {}", request.getUsername(), e.getMessage());
            return "Registration failed: " + e.getMessage();
        }
    }
    
    @Transactional
    public String registerDoctor(DoctorRegistrationRequest request) {
        log.info("Doctor registration attempt for username: {}", request.getUsername());
        
        if (userRepository.existsByUsername(request.getUsername())) {
            log.warn("Doctor registration failed - Username already exists: {}", request.getUsername());
            return "Username already exists";
        }
        
        // Validate password strength
        if (!PasswordConfig.isValidPassword(request.getPassword())) {
            log.warn("Doctor registration failed - Weak password for username: {}", request.getUsername());
            return PasswordConfig.getPasswordRequirements();
        }
        
        Optional<Role> doctorRoleOpt = roleRepository.findByName("DOCTOR");
        if (doctorRoleOpt.isEmpty()) {
            log.error("Doctor registration failed - Doctor role not found");
            return "Doctor role not found";
        }
        
        try {
            // Create user
            User user = new User();
            user.setUsername(request.getUsername());
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            user.setRole(doctorRoleOpt.get());
            
            User savedUser = userRepository.save(user);
            
            // Create doctor profile
            Doctor doctor = new Doctor();
            doctor.setUser(savedUser);
            doctor.setName(request.getName());
            doctor.setSpecialization(request.getSpecialization());
            doctor.setPhone(request.getPhone());
            doctor.setEmail(request.getEmail());
            doctor.setQualification(request.getQualification());
            doctor.setExperience(request.getExperience());
            doctor.setAddress(request.getAddress());
            doctor.setConsultationFee(request.getConsultationFee());
            
            doctorRepository.save(doctor);
            
            log.info("Doctor registered successfully: {}", request.getUsername());
            return "Doctor registered successfully";
            
        } catch (Exception e) {
            log.error("Doctor registration failed for username: {} - Error: {}", request.getUsername(), e.getMessage());
            return "Registration failed: " + e.getMessage();
        }
    }
    
    @Transactional
    public String registerPatient(PatientRegistrationRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            return "Username already exists";
        }
        
        Optional<Role> patientRoleOpt = roleRepository.findByName("PATIENT");
        if (patientRoleOpt.isEmpty()) {
            return "Patient role not found";
        }
        
        // Create user
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(patientRoleOpt.get());
        
        User savedUser = userRepository.save(user);
        
        // Create patient profile
        Patient patient = new Patient();
        patient.setUser(savedUser);
        patient.setName(request.getName());
        patient.setAge(request.getAge());
        patient.setGender(request.getGender());
        patient.setPhone(request.getPhone());
        patient.setEmail(request.getEmail());
        patient.setAddress(request.getAddress());
        patient.setDateOfBirth(request.getDateOfBirth());
        patient.setBloodGroup(request.getBloodGroup());
        patient.setEmergencyContact(request.getEmergencyContact());
        patient.setMedicalHistory(request.getMedicalHistory());
        
        patientRepository.save(patient);
        
        return "Patient registered successfully";
    }
}
