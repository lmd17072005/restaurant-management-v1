package com.example.restaurantmanagement.controller;

import com.example.restaurantmanagement.dto.request.ActorRequest;
import com.example.restaurantmanagement.dto.request.LoginRequest;
import com.example.restaurantmanagement.dto.response.ActorResponse;
import com.example.restaurantmanagement.dto.response.ApiResponse;
import com.example.restaurantmanagement.dto.response.AuthResponse;
import com.example.restaurantmanagement.config.security.JwtService;
import com.example.restaurantmanagement.entity.Actor;
import com.example.restaurantmanagement.entity.enums.Role;
import com.example.restaurantmanagement.exception.AppException;
import com.example.restaurantmanagement.exception.ErrorCode;
import com.example.restaurantmanagement.repository.ActorRepository;
import com.example.restaurantmanagement.service.ActorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Đăng nhập / Đăng ký")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtService jwtService;
    private final ActorService actorService;
    private final ActorRepository actorRepository;

    @Value("${app.jwt.expiration}")
    private long jwtExpiration;

    @PostMapping("/login")
    @Operation(summary = "Đăng nhập, nhận JWT token")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );
        } catch (BadCredentialsException e) {
            throw new AppException(ErrorCode.INVALID_CREDENTIALS);
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
        String token = jwtService.generateToken(userDetails);

        Actor actor = actorRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new AppException(ErrorCode.ACTOR_NOT_FOUND));

        AuthResponse authResponse = new AuthResponse();
        authResponse.setAccessToken(token);
        authResponse.setTokenType("Bearer");
        authResponse.setExpiresIn(jwtExpiration);
        authResponse.setActorId(actor.getId());
        authResponse.setUsername(actor.getUsername());
        authResponse.setRole(actor.getRole());

        return ResponseEntity.ok(ApiResponse.success("Đăng nhập thành công", authResponse));
    }

    @PostMapping("/register")
    @Operation(summary = "Đăng ký tài khoản (chỉ được tạo vai trò CUSTOMER)")
    public ResponseEntity<ApiResponse<ActorResponse>> register(@Valid @RequestBody ActorRequest request) {
        if (request.getRole() != Role.CUSTOMER) {
            throw new AppException(ErrorCode.ACCESS_DENIED, "Chỉ có thể tự đăng ký với vai trò CUSTOMER");
        }
        ActorResponse response = actorService.create(request);
        return ResponseEntity.status(201).body(ApiResponse.created(response));
    }
}

