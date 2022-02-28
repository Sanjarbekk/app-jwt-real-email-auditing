package com.example.appjwtrealemailauditing.service;

import com.example.appjwtrealemailauditing.entity.User;
import com.example.appjwtrealemailauditing.entity.enums.RoleName;
import com.example.appjwtrealemailauditing.payload.ApiResponse;
import com.example.appjwtrealemailauditing.payload.LoginDto;
import com.example.appjwtrealemailauditing.payload.RegisterDto;
import com.example.appjwtrealemailauditing.repository.RoleRepository;
import com.example.appjwtrealemailauditing.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    JavaMailSender javaMailSender;

    //@Autowired
    //AuthenticationManager authenticationManager;

    public ApiResponse registerUser(RegisterDto registerDto) {
        boolean existsByEmail = userRepository.existsByEmail(registerDto.getEmail());
        if(existsByEmail) {
            return new ApiResponse("Email had", false);
        }
        User user = new User();
        user.setFirstName(registerDto.getFirstName());
        user.setLastName(registerDto.getLastName());
        user.setEmail(registerDto.getEmail());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        user.setRoles(Collections.singleton(roleRepository.findByRoleName(RoleName.ROLE_USER)));
        user.setEmailCode(UUID.randomUUID().toString());
        userRepository.save(user);
        //sending Email
        sendEmail(user.getEmail(), user.getEmailCode());
        return new ApiResponse("Verify Email", true);

    }

    public Boolean sendEmail(String sendingEmail, String emailCode) {
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom("yulchiboy1997gmail.com");
            mailMessage.setTo(sendingEmail);
            mailMessage.setSubject("Tasdiqlash code");
            mailMessage.setText("<a href = 'http://localhost:9009/api/auth/verifyEmail?emailCode=" + emailCode + "&email=" + sendingEmail + "'> Tasdiqlash</a>");
            javaMailSender.send(mailMessage);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public ApiResponse verifyEmail(String emailCode, String email) {
        Optional<User> userOpt = userRepository.findByEmailAndEmailCode(email, emailCode);
        if(userOpt.isPresent()){
            User user = userOpt.get();
            user.setEnabled(true);
            user.setEmailCode(null);
            userRepository.save(user);
            return new ApiResponse("Checking succesfully", true);
        }
        return new ApiResponse("Error", false);
    }

    public ApiResponse login(LoginDto loginDto) {
        try {
           // authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getUserName(), loginDto.getPassword()));
        } catch(BadCredentialsException e) {

        }
        return null;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("dont found"));
    }
}