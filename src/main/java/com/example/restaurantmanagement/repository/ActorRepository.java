package com.example.restaurantmanagement.repository;

import com.example.restaurantmanagement.entity.Actor;
import com.example.restaurantmanagement.entity.enums.ActorStatus;
import com.example.restaurantmanagement.entity.enums.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ActorRepository extends JpaRepository<Actor, Long> {

    Optional<Actor> findByUsername(String username);

    Optional<Actor> findByEmail(String email);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByPhoneNumber(String phoneNumber);

    // Lọc theo vai trò, có phân trang
    Page<Actor> findByRole(Role role, Pageable pageable);

    // Lọc theo trạng thái, có phân trang
    Page<Actor> findByStatus(ActorStatus status, Pageable pageable);

    // Tìm kiếm đa điều kiện: keyword (tên/email/sdt) + lọc role + lọc status
    @Query("""
            SELECT a FROM Actor a
            WHERE (:keyword IS NULL
                OR LOWER(a.fullName)    LIKE LOWER(CONCAT('%', :keyword, '%'))
                OR LOWER(a.email)       LIKE LOWER(CONCAT('%', :keyword, '%'))
                OR a.phoneNumber        LIKE CONCAT('%', :keyword, '%'))
            AND (:role   IS NULL OR a.role   = :role)
            AND (:status IS NULL OR a.status = :status)
            """)
    Page<Actor> search(
            @Param("keyword") String keyword,
            @Param("role")    Role role,
            @Param("status")  ActorStatus status,
            Pageable pageable
    );
}

