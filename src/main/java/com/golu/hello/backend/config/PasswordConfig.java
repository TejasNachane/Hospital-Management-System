package com.golu.hello.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.regex.Pattern;

@Configuration
public class PasswordConfig {
    
    /**
     * Password policy: 
     * - Minimum 8 characters
     * - At least one uppercase letter
     * - At least one lowercase letter
     * - At least one digit
     * - At least one special character
     */
    private static final String PASSWORD_PATTERN = 
        "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";
    
    private static final Pattern pattern = Pattern.compile(PASSWORD_PATTERN);
    
    public static boolean isValidPassword(String password) {
        return pattern.matcher(password).matches();
    }
    
    public static String getPasswordRequirements() {
        return "Password must be at least 8 characters long and contain: " +
               "one uppercase letter, one lowercase letter, one digit, " +
               "and one special character (@#$%^&+=)";
    }
    
    /**
     * Custom password encoder with strength validation
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new PasswordEncoder() {
            private final PasswordEncoder delegate = new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder(12);
            
            @Override
            public String encode(CharSequence rawPassword) {
                // Validate password strength before encoding
                if (!isValidPassword(rawPassword.toString())) {
                    throw new IllegalArgumentException(getPasswordRequirements());
                }
                return delegate.encode(rawPassword);
            }
            
            @Override
            public boolean matches(CharSequence rawPassword, String encodedPassword) {
                return delegate.matches(rawPassword, encodedPassword);
            }
        };
    }
}
