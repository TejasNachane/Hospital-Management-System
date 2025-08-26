package com.golu.hello.backend.service;

import com.golu.hello.backend.entity.User;
import com.golu.hello.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SecurityService {
    
    private final UserRepository userRepository;
    
    /**
     * Get the currently authenticated user
     */
    public Optional<User> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication == null || !authentication.isAuthenticated() || 
            authentication.getPrincipal().equals("anonymousUser")) {
            return Optional.empty();
        }
        
        String username = authentication.getName();
        return userRepository.findByUsername(username);
    }
    
    /**
     * Check if current user has a specific role
     */
    public boolean hasRole(String roleName) {
        return getCurrentUser()
            .map(user -> user.getRole().getName().equals(roleName))
            .orElse(false);
    }
    
    /**
     * Check if current user is admin
     */
    public boolean isAdmin() {
        return hasRole("ADMIN");
    }
    
    /**
     * Check if current user is doctor
     */
    public boolean isDoctor() {
        return hasRole("DOCTOR");
    }
    
    /**
     * Check if current user is patient
     */
    public boolean isPatient() {
        return hasRole("PATIENT");
    }
    
    /**
     * Check if current user can access patient data
     */
    public boolean canAccessPatientData(Long patientId) {
        Optional<User> currentUser = getCurrentUser();
        if (currentUser.isEmpty()) {
            return false;
        }
        
        // Admin can access all data
        if (isAdmin()) {
            return true;
        }
        
        // Doctors can access patient data
        if (isDoctor()) {
            return true;
        }
        
        // Patients can only access their own data
        if (isPatient() && currentUser.get().getPatient() != null) {
            return currentUser.get().getPatient().getId().equals(patientId);
        }
        
        return false;
    }
    
    /**
     * Check if current user can access doctor data
     */
    public boolean canAccessDoctorData(Long doctorId) {
        Optional<User> currentUser = getCurrentUser();
        if (currentUser.isEmpty()) {
            return false;
        }
        
        // Admin can access all data
        if (isAdmin()) {
            return true;
        }
        
        // Doctors can access their own data
        if (isDoctor() && currentUser.get().getDoctor() != null) {
            return currentUser.get().getDoctor().getId().equals(doctorId);
        }
        
        return false;
    }
    
    /**
     * Get current user ID
     */
    public Optional<Long> getCurrentUserId() {
        return getCurrentUser().map(User::getId);
    }
    
    /**
     * Get current user role
     */
    public Optional<String> getCurrentUserRole() {
        return getCurrentUser().map(user -> user.getRole().getName());
    }
}
