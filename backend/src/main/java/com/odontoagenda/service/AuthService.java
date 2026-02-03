package com.odontoagenda.service;

import com.odontoagenda.dto.request.LoginRequest;
import com.odontoagenda.dto.request.RegisterPatientRequest;
import com.odontoagenda.dto.response.AuthResponse;
import com.odontoagenda.exception.BusinessException;
import com.odontoagenda.model.entity.Patient;
import com.odontoagenda.model.entity.User;
import com.odontoagenda.model.enums.UserRole;
import com.odontoagenda.repository.PatientRepository;
import com.odontoagenda.repository.UserRepository;
import com.odontoagenda.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Serviço responsável pela autenticação e registro de usuários.
 */
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PatientRepository patientRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    /**
     * Registra um novo paciente no sistema.
     */
    @Transactional
    public AuthResponse registerPatient(RegisterPatientRequest request) {
        // Verifica se o e-mail já está cadastrado
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException("E-mail já cadastrado");
        }

        // Cria o usuário
        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .fullName(request.getFullName())
                .phone(request.getPhone())
                .role(UserRole.PATIENT)
                .active(true)
                .build();

        user = userRepository.save(user);

        // Cria o paciente associado ao usuário
        Patient patient = Patient.builder()
                .user(user)
                .cpf(request.getCpf())
                .build();

        patientRepository.save(patient);

        // Gera o token JWT
        String token = jwtService.generateToken(user);

        return AuthResponse.builder()
                .token(token)
                .email(user.getEmail())
                .fullName(user.getFullName())
                .role(user.getRole())
                .userId(user.getId())
                .build();
    }

    /**
     * Realiza o login de um usuário.
     */
    public AuthResponse login(LoginRequest request) {
        try {
            // Autentica o usuário
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );

            // Busca o usuário
            User user = userRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new BusinessException("Usuário não encontrado"));

            // Gera o token JWT
            String token = jwtService.generateToken(user);

            return AuthResponse.builder()
                    .token(token)
                    .email(user.getEmail())
                    .fullName(user.getFullName())
                    .role(user.getRole())
                    .userId(user.getId())
                    .build();

        } catch (AuthenticationException e) {
            throw new BusinessException("E-mail ou senha inválidos");
        }
    }
}
