package com.example.restaurantmanagement.service.impl;

import com.example.restaurantmanagement.dto.request.ActorRequest;
import com.example.restaurantmanagement.dto.request.ActorUpdateRequest;
import com.example.restaurantmanagement.dto.response.ActorResponse;
import com.example.restaurantmanagement.entity.Actor;
import com.example.restaurantmanagement.entity.enums.ActorStatus;
import com.example.restaurantmanagement.entity.enums.Role;
import com.example.restaurantmanagement.exception.AppException;
import com.example.restaurantmanagement.exception.ErrorCode;
import com.example.restaurantmanagement.mapper.ActorMapper;
import com.example.restaurantmanagement.repository.ActorRepository;
import com.example.restaurantmanagement.service.ActorService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ActorServiceImpl implements ActorService, UserDetailsService {

    private final ActorRepository actorRepository;
    private final ActorMapper actorMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public ActorResponse create(ActorRequest request) {
        if (actorRepository.existsByUsername(request.getUsername()))
            throw new AppException(ErrorCode.USERNAME_ALREADY_EXISTS);
        if (request.getEmail() != null && actorRepository.existsByEmail(request.getEmail()))
            throw new AppException(ErrorCode.EMAIL_ALREADY_EXISTS);
        if (request.getPhoneNumber() != null && actorRepository.existsByPhoneNumber(request.getPhoneNumber()))
            throw new AppException(ErrorCode.PHONE_ALREADY_EXISTS);

        Actor actor = actorMapper.toEntity(request);
        actor.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        actor.setStatus(ActorStatus.ACTIVE);

        return actorMapper.toResponse(actorRepository.save(actor));
    }

    @Override
    @Transactional(readOnly = true)
    public ActorResponse getById(Long id) {
        return actorMapper.toResponse(findById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ActorResponse> getAll(Pageable pageable) {
        return actorRepository.findAll(pageable).map(actorMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ActorResponse> search(String keyword, Role role, ActorStatus status, Pageable pageable) {
        return actorRepository.search(keyword, role, status, pageable).map(actorMapper::toResponse);
    }

    @Override
    @Transactional
    public ActorResponse update(Long id, ActorUpdateRequest request) {
        Actor actor = findById(id);

        if (request.getEmail() != null
                && !request.getEmail().equals(actor.getEmail())
                && actorRepository.existsByEmail(request.getEmail()))
            throw new AppException(ErrorCode.EMAIL_ALREADY_EXISTS);

        if (request.getPhoneNumber() != null
                && !request.getPhoneNumber().equals(actor.getPhoneNumber())
                && actorRepository.existsByPhoneNumber(request.getPhoneNumber()))
            throw new AppException(ErrorCode.PHONE_ALREADY_EXISTS);

        actorMapper.updateEntity(request, actor);
        return actorMapper.toResponse(actorRepository.save(actor));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!actorRepository.existsById(id))
            throw new AppException(ErrorCode.ACTOR_NOT_FOUND);
        actorRepository.deleteById(id);
    }

    /**
     * Dùng cho Spring Security khi xác thực JWT.
     * ActorServiceImpl implement luôn UserDetailsService — không cần class riêng.
     */
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Actor actor = actorRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy user: " + username));

        if (actor.getStatus() == ActorStatus.INACTIVE)
            throw new AppException(ErrorCode.ACTOR_INACTIVE);

        return User.builder()
                .username(actor.getUsername())
                .password(actor.getPasswordHash())
                .roles(actor.getRole().name())
                .build();
    }

    // ---- helper ----
    private Actor findById(Long id) {
        return actorRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.ACTOR_NOT_FOUND));
    }
}

